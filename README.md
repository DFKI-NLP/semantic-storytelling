# Semantic Relations between Text Segments for Semantic Storytelling: Corpus and Experiments

This repository contains the code and dataset for the paper `Semantic Relations between Text Segments for Semantic Storytelling: Corpus and Experiments` (currently under peer-review).

## Content

The `notebooks/` folder contains the [jupyter notebook](https://jupyter.org/) script files to extract sentence pairs from Wikinews articles, convert extracted sentence pairs to CAS format to import them into the INCEpTION platform, store exported (annotated) projects from INCEpTION in datasets with folds, and train and fine-tune Language Models with these datasets using the [huggingface transformer](https://huggingface.co/) and [pytorch](https://pytorch.org/) libraries.

In `inception/` is a fork of the [INCEpTION Plattform](https://inception-project.github.io/) with a custom sentence relation editor used in the thesis and experiments to annotate the extracted sentence pairs from Wikinews articles.

# Notebooks

## Installation
All notebooks need a [python 3.6](https://www.python.org/downloads/) and [jupyter notebook](https://jupyter.org/install) environment. The use of an environment management system like [conda](https://docs.conda.io/en/latest/) is recommended.

To install the dependencies please run the `requirements.txt` in `notebooks/` and `notebooks/candidates/` respectively with the command:

```
pip3 install -r requirements.txt
```

## Extracting Sentence Pairs
In `notebooks/candidates/` are the scripts for extracting sentence pairs from Wikinews articles written by Malte Ostendorff.

## Converting Sentence Pairs to CAS
The Notebook located in `notebooks/import.ipynb` transforms the extracted sentence pairs to a custom UIMA CAS format. The corresponding CSV file as input and the XMI file as output need to be assigned in the Properties Section.

### Imports
CSV file with candidate pairs and columns:
```
a_doc_id, a_start, a_end, a_text, a_url, a_title, a_categories, b_doc_id, b_start, b_end, b_text, b_url, b_title, b_categories
```

### Exports
XMI import file for INCEpTION platform in UIMA CAS Format with `Sentence` and `SentenceRelation` Annotation (set to 'unset').


## Creating Trainingsdata from INCEpTION Export
The Notebook located in `notebooks/create_dataset.ipynb` creates training and test datasets with a stratified fold that keeps the original label distribution for each fold. The properties for the dataset can be defined in the notebook like the amount of folds, label classes and input strategies. There is a preset of four input strategies (`input_strategy` a number between `1` and `4`):

1. Sentence (S)
2. Title + Sentence (TS)
3. Title + Sentence + Date (TSD)
4. Sentence + Title + Date (STD)
*Sentence* is the extracted Sentence from News Article, *Title* is the News Article Title and *Date* is the publishing date of the article.

Also the labels can be defined here. If a subset of original label classes should be used, the variable `subset_labels` should be set, so that all sentence pairs with a label that is not contained in the subset are assigned to the last label of the subset, which should be a group label like `others`.

### Imports
INCEpTION Export CAS XMI Files at `notebooks/data/inception/`. File names should contain either "random", "nsp" or "similarity" to map the corresponding pre-matching algorithm.

### Exports
Train/Test- and Fold-Splits of Trainingsdata as
```
notebooks/data/export/{input_strategy}/train.{fold}.csv
```
and
```
notebooks/data/export/{input_strategy}/test.{fold}.csv
```
**Note**: If a subset of the original label classes is used (`subset_labels == TRUE`), the folder `export` is replaced with `export_subset`

`fold` is the index of folds which is in this scenario (4 folds) a number between `0` and `3`.

**In addition**, a visualization of the distribution of pre-matching algorithms and labels is stored in the same folder.

## Training a Model

The Notebook located in `notebooks/train_datasets.ipynb` fine-tunes the pretrained language model with the trainings dataset and evaluates the result with the test dataset. The Model and other properties like epochs and batch size can be changed in the notebook. The labels and fold numbers should match the ones from the `create_dataset` notebook. When setting `input_strategy`, the notebook will automatically load the corresponding created datasets.

### Imports
The train and test folds of the dataset depending on `input_strategy` and `subset_labels` (see above).

### Exports
Evaluation metrics at
```
notebooks/data/eval/{input_strategy}/{model_name}_epoch_{num_epoch}.csv
```
Pytorch Model at
```
notebooks/data/model/{input_strategy}/{model_name}/epoch_{num_epoch}/fold_{fold}/
```
Whereas `model_name` is the name or checkpoint of the huggingface hosted pretrained model, `num_epoch` the total amount of epochs and `fold` the index of the corresponding fold (the models of all folds are saved).
**Note**: If a subset of the original label classes is used (`subset_labels == TRUE`), the folder `eval` is replaced with `eval_subset` and `model` with `model_subset` respectively.

## Efficiently Train all Models

The Notebook located in `notebooks/train_datasets_all.ipynb` fine-tunes a list of pretrained language models with the training dataset and evaluates the results with the test dataset. The notebook performs the training process optimized in terms of GPU RAM. In contrast to the better readable `train_datasets.ipynb` notebook, this notebook frees memory after each fold, so that the available memory is better utilized. The Model Settings apply to every trainingsprocess. The lists `input_strategies`, `subset_labels_types` and `model_checkpoints` define all models to be trained.

The Imports and Exports are similar to "Training a Model" for all defined models.

## Evaluate all Models
The Notebook `notebooks/eval.ipynb` displays all evaluation metrics that have been exported while training for the full classification problem or the classification of the subset labels. It shows a table with all metrics and a summarized table for each model. At the end in the Summary Section, a summarized table with all models and the core metrics is displayed and two boxplot diagrams containing information about the accuracy distribution depending on the pretrained model and input strategy. The boxplot diagrams are automatically saved (see Exports). The variables `subset_labels`, `input_strategies` and `min_epoch` can be used to filter the results.

### Imports
Training Evaluation metrics as csv files (all csv files from
`notebooks/data/eval/`
).

### Exports
Boxplot diagrams for input strategies (`notebooks/data/eval/boxplot.svg`) and models (`notebooks/data/eval/boxplot_model.svg`).

**Note**: If a subset of the original label classes is used (`subset_labels == TRUE`), the folder `eval` is replaced with `eval_subset`.

## Predict with Model
The Notebook `notebooks/predict_dataset.ipynb` can be used to load a saved model and predict and evaluate test datasets. If the model properties (`base_model`, `num_epoch`, `input_strategy`, `subset_labels` and `fold`) are set AND the model was trained before, the script will use the model of the `fold` and predict and evalute on the test data of this fold. A list of successes, fails and all cases are displayed.

### Imports
Model Checkpoint depending on model properties
```
notebooks/data/model/{input_strategy}/{model_name}/epoch_{num_epoch}/fold_{fold}/
```
and validation dataset of this fold at
```
notebooks/data/export/{input_strategy}/test.{fold}.csv
```

# INCEpTION

Running Version of INCEpTION in version 0.17.2 (forked) with a custom Sentence Relation Annotation Editor that overwrites the standard HTML Annotation Editor (`inception-html-editor`). The Editor is able to work with plain Text files and prematched UIMA CAS XMI files (produced by the notebooks above). When working with plain Text files, the editor automatically splits the content into sentences and produces every possible sentence pair that can be annotated.

## Installation

Build and Run with Tomcat the `inception-app-webapp:war` Artifact in IDE: See [Developer Documentation of INCEpTION](https://inception-project.github.io/releases/21.1/docs/developer-guide.html). Build with Maven using command line:
```
# (runs all the unit tests... takes some time)
mvn package

# no test
mvn package -Dmaven.test.skip=true
```

A Docker Image with the latest running version can be pulled at `michaelraring/inception`.

## INCEpTION Configuration

In order to use the Sentence Relation Annotion Editor as expected, please make sure to set up your INCEpTION Project as expected.

1. Please Setup your Relation Label Inventory. Go in your Project Settings to `Tagsets` and add a new Tagset. You can freely name and add Tags. BUT: Please make sure you add a placeholder Tag for untagged Relations with name `unset`!
2. Go to `Layers` and add a Layer with name `Sentence` (The name can be different, BUT the internal name needs to be `webanno.custom.Sentence`) and type `Span`. For the `Behaviors` Settings it is suggested to choose `Granularity: Token-level` (because INCEpTION's Sentence splitting is not always correct) and `Granularity: None`. You are free to choose any Features to add. They will be persisted by the Annotation. For the MetaData Panel it is recommend to add `title` and `url` as strings.
3. Add an other Layer of type `Relation` with name `SentenceRelation` (again it is only important that the internal name is `webanno.custom.SentenceLayer`) and attach it to the `Sentence` layer. Overlap should be set to `any` this time. This Layer needs a Feature `label` (internal name) from type String and attach it to the Tagset you created in the first step.

Then you can add Documents to annotate. Documents can be plain text or UIMA CAS XMI files.


## Source Code Description

The source code of the Sentence Relation Annotation Editor can be found at
```
inception/inception-html-editor/src/main/java/de/tudarmstadt/ukp/inception/htmleditor/

```

### Important Files
Here are only the files described that have been written or modified from the original html editor.
- `HTMLAnnotationEditor.java` : Main Java source code
- `HTMLAnnotationEditor.html` : HTML Layout of Editor
- `ActiveCSSPropertyModel.java` : Wicket Model for changing the CSS property of the Relation arrows (switch gray and green color)
- `textRelationsAnnotator/` : JS and CSS files included in the editor. The JS code is empty.
- `statistic/` :
	- `StatisticPanel.java` and `StatisticPanel.html` : code and template for statistic dialog showing label distribution.
	- `StatisticValuePanel.java`, `StatisticValuePanel.html` and `StatisticValueModel.java` : code, template and Wicket model for each label/item in statistic dialog
	- `StatisticIndicatorCSSModel.java` : Wicket Model to change CSS style for each `StatisticValuePanel`
- `progress/` :
	- `ProgressPanel.java` and `ProgressPanel.html` : code and template for progress bar
	- `ProgressModel.java`, `ProgressLabelModel.java` and `ProgressIndicatorCSSModel.java` : Wicket Models for Progress value, string label and CSS style (green bar)
- `model/` :
	- `Pair.java` : Java Class to define a Pair of Sentences (left and right Sentence can be permuted)
	- `RelationFeatureSupplier.java` : Supplier needed to create SentenceRelation-Adapter to CAS to store Annotations
	- `TextRelation.java` : Java Class storing a Text Relation (both strings of sentences and relation Tag) to easier display Relation and Sentences
- `meta/` :
	- `MetaDataPanel.java` and `MetaDataPanel.html` : code and template of MetaData Panel for Sentences (showing Link to the Source Document and Title when `title` and `url` is set on the `Sentence` Annotation)
	- `SegmentAnnotationFeatureModel.java` : Dynamic Wicket Model for a specific Feature of a `Sentence` Annotation. Used in MetaDataPanel for Features `title` and `url`
- `filter/` : `FilterPanel.java` and `FilterPanel.html` : code and template for Dialog to filter Sentence pairs with specific Labels when using pairwise navigation. Good for corrections on the dataset.

## How to cite

TODO

## License

MIT
