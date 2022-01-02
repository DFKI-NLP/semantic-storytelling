/**
 * Create text layers which enable users to select texts.
 */
import { customizeAnalyzeResult, extractMeta } from './util/analyzer'

/**
 * Text layer data.
 */
let pages

/**
 * Setup text layers.
 */
export function setup (analyzeData) {
  // Create text layers data.
  pages = customizeAnalyzeResult(analyzeData)
}

/**
 * Find index between characters in the text.
 * Used for zero-width span annotations.
 * @param page - the page number.
 * @param point - { x, y } coords.
 * @return {*} - The nearest text index to the given point.
 */
window.findIndex = function (page, point) {

  const metaList = pages[page - 1].meta

  for (let i = 0, len = metaList.length; i < len; i++) {
    const info = metaList[i]

    if (!info) {
      continue
    }

    const { position, char, x, y, w, h } = extractMeta(info)

    if (y <= point.y && point.y <= (y + h)) {
      if (x <= point.x && point.x <= x + w / 2) {
        return position
      } else if (x + w / 2 < point.x && point.x <= x + w) {
        return position + 1
      }
    }
  }

  return null
}

/**
 * Find the text.
 * @param page - the page number.
 * @param point - { x, y } coords.
 * @returns {*} - The text data if found, whereas null.
 */
window.findText = function (page, point) {

  const metaList = pages[page - 1].meta

  for (let i = 0, len = metaList.length; i < len; i++) {
    const info = metaList[i]

    if (!info) {
      continue
    }

    const { position, char, x, y, w, h } = extractMeta(info)

    // is Hit?
    if (x <= point.x && point.x <= (x + w)
      && y <= point.y && point.y <= (y + h)) {
      return { position, char, x, y, w, h }
    }
  }

  return null
}

/**
 * Find the texts.
 * @param page - the page number.
 * @param startPosition - the start position in pdftxt.
 * @param endPosition - the end position in pdftxt.
 * @param allowZeroWidth - whether zero-width spans are allowed or not, required for range usage
 * @returns {Array} - the texts.
 */
window.findTexts = function (page, startPosition, endPosition, allowZeroWidth) {

  const items = []

  if (startPosition == null || endPosition == null) {
    return items
  }

  const metaList = pages[page - 1].meta

  let inRange = false

  for (let index = 0, len = metaList.length; index < len; index++) {

    const info = metaList[index]

    if (!info) {
      if (inRange) {
        items.push(null)
      }
      continue
    }

    const data = extractMeta(info)
    var last = data
    const { position } = data

    // if zero-width spans are allowed interprete ranges different from PDFAnno default
    // [3,3] is zero-width, [3,4] is one character
    if (allowZeroWidth) {
      if (startPosition === endPosition && startPosition === position) {
        data.w = 1
        data.x = data.x - 1
        items.push(data)
        break
      }

      if (startPosition <= position && position < endPosition) {
        inRange = true
        items.push(data)
      }

      if (position === endPosition) {
        break
      }

      if (last.position < data.position) last = data
    // if zero-width spans are not allowed interprete ranges as PDFAnno does usually
    // [3,3] is one character, [3,4] are two characters
    } else {
      if (startPosition <= position) {
        inRange = true
        items.push(data)
      }

      if (endPosition <= position) {
        break
      }
    }
  }

  // handle case where position is the very last index for a zero-width span
  // which does not exist in the pdfextract file and therefore cannot be found
  // use the previous character box and "append" the zero-width span after it
  if (items.length === 0 && startPosition === endPosition && page === pages.length && allowZeroWidth) {
    items.push({
      position: startPosition,
      page: last.page,
      type: last.type,
      char: '',
      x: last.x + last.w,
      y: last.y,
      w: 1,
      h: last.h
    })
  }

  return items
}

/**
 * Merge user selections.
 */
window.mergeRects = function (rects) {

  // Remove null.
  rects = rects.filter(rect => rect)

  if (rects.length === 0) {
    return []
  }

  // Normalize.
  rects = rects.map(rect => {
    rect.top = rect.top || rect.y
    rect.left = rect.left || rect.x
    rect.right = rect.right || (rect.x + rect.w)
    rect.bottom = rect.bottom || (rect.y + rect.h)
    return rect
  })

  // a virtical margin of error.
  const error = 5 * scale()

  let tmp = convertToObject(rects[0])
  let newRects = [tmp]
  for (let i = 1; i < rects.length; i++) {

    // Same line -> Merge rects.
    if (withinMargin(rects[i].top, tmp.top, error)) {
      tmp.top    = Math.min(tmp.top, rects[i].top)
      tmp.left   = Math.min(tmp.left, rects[i].left)
      tmp.right  = Math.max(tmp.right, rects[i].right)
      tmp.bottom = Math.max(tmp.bottom, rects[i].bottom)
      tmp.x      = tmp.left
      tmp.y      = tmp.top
      tmp.width  = tmp.right - tmp.left
      tmp.height = tmp.bottom - tmp.top

      // New line -> Create a new rect.
    } else {
      tmp = convertToObject(rects[i])
      newRects.push(tmp)
    }
  }

  return newRects
}

/**
 * Convert a DOMList to a javascript plan object.
 */
function convertToObject (rect) {
  return {
    top    : rect.top,
    left   : rect.left,
    right  : rect.right,
    bottom : rect.bottom,
    x      : rect.x,
    y      : rect.y,
    width  : rect.width,
    height : rect.height
  }
}

/**
 * Check the value(x) within the range.
 */
function withinMargin (x, base, margin) {
  return (base - margin) <= x && x <= (base + margin)
}

/**
 * Returns the scaling factor of the PDF. If the PDF has not been drawn yet, a factor of 1 is
 * assumed.
 */
function scale () {
  if (window.PDFView.pdfViewer.getPageView(0) === undefined) {
    return 1;
  } else {
    return window.PDFView.pdfViewer.getPageView(0).viewport.scale;
  }
}
