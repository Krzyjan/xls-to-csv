package org.krzyjan.utilities

import java.io.InputStream

import scala.annotation.tailrec
import scala.collection.immutable.Vector

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory


class POIXLStoCSV extends XLStoCSV {
  def convert(in: InputStream): Vector[Vector[String]] = POIXLStoCSV.convert(in)
}

object POIXLStoCSV {
  val dataFormatter = new DataFormatter(true)

  def convert(in: InputStream): Vector[Vector[String]] = {
    val workbook = WorkbookFactory.create(in)
    val formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator()
    val numberOfSheets = workbook.getNumberOfSheets()

    def formatCell(c: Cell): String = {
      if (c.getCellType() != Cell.CELL_TYPE_FORMULA) {
        dataFormatter.formatCellValue(c)
      } else {
        dataFormatter.formatCellValue(c, formulaEvaluator)
      }
    }

    @tailrec def processCells(resultRow: Vector[String], r: Row, cellIdx: Int, maxCells: Int): Vector[String] = {
      if (cellIdx < maxCells) {
        val cell = r.getCell(cellIdx)
        if (cell == null) {
          processCells(resultRow :+ "", r, cellIdx + 1, maxCells)
        } else {
          processCells(resultRow :+ formatCell(cell), r, cellIdx + 1, maxCells)
        }
      } else {
        resultRow
      }
    }

    @tailrec def processRows(resultRows: Vector[Vector[String]], s: Sheet, rowIdx: Int, maxRows: Int): Vector[Vector[String]] = {
      if (rowIdx < maxRows) {
        val row = s.getRow(rowIdx)
        if (row != null) {
          processRows(resultRows :+ processCells(Vector[String](), row, 0, row.getLastCellNum()), s, rowIdx + 1, maxRows)
        } else {
          // Note that we add an empty row here to preserve the original formatting
          processRows(resultRows :+ Vector[String](), s, rowIdx + 1, maxRows)
        }
      } else {
        resultRows
      }
    }

    @tailrec def processSheets(resultSheet: Vector[Vector[String]], idx: Int): Vector[Vector[String]] = {
      if (idx < numberOfSheets) {
        val sheet = workbook.getSheetAt(idx)
        if (sheet.getPhysicalNumberOfRows() > 0) {
          processSheets(resultSheet ++ processRows(Vector(), sheet, 0, sheet.getLastRowNum() + 1), idx + 1)
        } else {
          processSheets(resultSheet, idx + 1)
        }
      } else {
        resultSheet
      }
    }

    processSheets(Vector(), 0)
  }  
}