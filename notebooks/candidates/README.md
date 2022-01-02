# Storytelling Candidates
Semantic Storytelling: Candiate retrieval and gold standard.

Semantic storytelling. See papers:
- http://ceur-ws.org/Vol-2535/paper_18.pdf
- http://ceur-ws.org/Vol-2342/paper8.pdf


## Requirements
- Python 3.7

## Install

```bash
git clone https://github.com/malteos/storytelling-candidates.git
cd storytelling-candidates
conda create -n storytelling-candidates python=3.7
conda activate storytelling-candidates
pip install -r requirements.txt
```

## Candidate retrieval

### Task context

> One of the goals of our Semantic Storytelling system is to aid knowledge workers in selecting relevant pieces of content, e. g., the app editor who wants to curatestories for the app.
> Step 1: Determine the Relevance of a Segment for a Topic
> Step 2: Determine the Importance of a Segment
> Step 3: Semantic or Discourse Relation between two Segments

For step 3, pairs of text segments are subject to a classification. To reduce the search space, we need to perform candidate retrieval as preprocessing step.
In other words, we need to find pairs of segments that are likely to share a semantic relation.
A text segment can be of arbitray length (sentenes, paragraphs, sections, ...). However, at first we will consider only sentences as text segments.


### Task description

- Input: Wikinews dump https://dumps.wikimedia.org/enwikinews/
- Output: 
   - A list of candidate pairs including a score: `sentence 1 id, sentence 2 id, <score [0-1]>`
   - Meta data for each sentence: `sentence id, article id, position in article (offset, end), text`

#### Extract sentences from Wikinews:
- Download the latest Wikinews dump that contains article data. Check for ending `-pages-articles.xml.bz2`
- Parse dump and extract article text. Remove non-text content (e.g., references, tables, images etc.) See https://github.com/attardi/wikiextractor or https://radimrehurek.com/gensim/scripts/segment_wiki.html
- Split the article text into sentences https://radimrehurek.com/gensim_3.8.3/summarization/textcleaner.html#gensim.summarization.textcleaner.split_sentences

##### Build meta data file
To preprocess the original Wikinews dump and generate the meta data file run the following command:
```
# command
python data_retrieval_scripts/meta_data_extraction.py <Wikipedia dump> <Output file (without file extension)> <Mininmal number of characters per sentence> <Maximal number of characters per sentence>

# example
python data_retrieval_scripts/meta_data_extraction.py /data/datasets/wikinews_en/20201201/enwikinews-20201201-pages-meta-current.xml.bz2 /data/experiments/hensel/storytelling-candidates/data/meta_data 50 1000
```
The meta data can be found as `meta_data.tsv` and `meta_data.docs.tsv` in the directory `/data/experiments/hensel/storytelling-candidates/data/` on `serv-9212`.

#### Candiate retrieval

##### Vector similarity

- Convert each sentence into a vector representation, e.g., average of fasttext word vectors https://radimrehurek.com/gensim/models/fasttext.html
- Perform a k-nearest neighbor search to find other sentences with similar vectors https://radimrehurek.com/gensim/models/keyedvectors.html#gensim.models.keyedvectors.KeyedVectors.most_similar
- Similar score would be the candidate score in the output

To generate the sentence vectors and retrieve the candidate sentence pairs run:
```
python Sentence_pair_selection.py --sentences <inputfile> --vectors <vectors file name.bin> --pairs <pairs outputfile name> --number-pairs <n> --random-sample [yes/no]
```
The sentence vectors extracted candidate pairs can be found in the directory data/experiments/hensel/storytelling/data.

##### Next sentence prediction

- Use a pretrained Transformer language model (like BERT) to predict whether one sentence can be the next one to another sentence.
- See example https://huggingface.co/transformers/model_doc/bert.html#bertfornextsentenceprediction
- Predicted probability would be the candidate score in the output

Candidate pairs can be extracted by running the script `next_sentence_prediction.py` (which can be found in `data_retrieval_scripts`) with the following command:
```
#Command
$ python3 data_retrieval_scripts/next_sentence_prediction.py <path_to_model outputdir> <meta_data_directory> <meta_datafile.tsv> <pair_directory> <sentence_pairs_file.tsv> save <output_directory>

# Example
$ python3 data_retrieval_scripts/next_sentence_prediction.py predict output/nsp/bert-base-cased/ ./output/nsp data meta_data-12-02-21.tsv data sentence_pairs-12-02-21.tsv
```


## Gold standard retrieval

Semantic storytelling aims to create novel stories based on existing text snippets (story units).
To evaluate our methodology, we need a *gold standard* or *reference corpus*  of news articles that were created in a similar manner. 
Such a news article would contain several text snippets of other articles and some additional unique content.
Naively, you can think about it as plagiarised articles. 
Authors would copy-write or even copy-paste from other articles to create their own new article.

Finding those articles by hand is unfeasible. 
Thus, we will try the following automatic approach:

- Input: Wikinews dump (see above)
- Output: JSONL-file `{"wikinews_article_id": "...", "score": 1.0, "plagiarised_sentences": [{"text": "...", "source_text": "..." "source_wikinews_article_id": "..."}]}`

Process Wikinews dump as above.
- Build sentence meta data  `article_id; sentence_id; text; position; ...`
- Build sentence vectors `article_id; sentence_id; sentence_vector`
- For each article:
   - Compare each sentence with all other sentences from the corpus
   - plagiarised sentences are sentences with a vector similarity > X (X should be a parameter)
   - An article is plagiarised if it has more than Y plagiarised sentences (Y should be a parameter)

Lastly, we will manually validate the automatically extracted gold standard.

Additional ideas:
- Filter out too common sentences
- Check for sentence patterns (where do the plagiarised sentences occur? beginning vs end of article? ...)
- Make use of article meta data (publication date: min. time span between articles, article category, entities)

### Retrieve JSON with plagiarized articles and sentences (search_plagiarized.py)

- Step 1: Search for plagiarized (=semantically similar to a certain degree) sentences by computing cosine similarities
```
$ python search_plagiarized.py search_plagiarized_sents Sentence_vectors-180121.bin Meta_data.tsv 0.99 plagiarized_0-99.json
```
You may use Multithreading for Step 1:
```
$ python search_plagiarized.py call_multithreading 10 Meta_data.tsv Sentence_vectors-180121.bin 0.99 plagiarized_0-99.json 5
```
- Step 2: Create JSON file with plagiarized articles and their respective scores:

   total_n_sources (total number of sources), n_uni_sources (number of unique
   sources), unique_score ((total_n_sources/n_uni_sources)/total_n_sources), 
   total_p_sents (total number of plagiarized sentences in article)
```
$ python search_plagiarized.py calculate_unique_score plagiarized_0-99.json doc_scores.json  
```
- Step 3: Transform meta data TSV file which contains sentences + meta data (but not article title + article text) to JSON 
```
$ python search_plagiarized.py tsv_to_json Meta_data.tsv meta.json
```
- Step 4: Create final JSON file
```
$ python search_plagiarized.py create_final_table meta.json doc_scores.json plagiarized_0-99.json doc_title_text.json results.json
```

## Other TODOs

- Integrate co-reference resoultion

 


