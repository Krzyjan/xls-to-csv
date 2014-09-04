package org.krzyjan.utilities

import java.io.FileInputStream
import java.io.StringBufferInputStream

import org.krzyjan.common.UnitFlatSpec

class POIXLStoCSVTest extends UnitFlatSpec {
  trait Values {
    val fileSep = System.getProperty("file.separator");
    val topDir = "src/test/resources"

    val emptySpreadSheet = topDir + fileSep + "Empty.xls"
    val onelineSpreadSheet = topDir + fileSep + "OneLine.xls"
    val multiLineSpreadSheet = topDir + fileSep + "MultiLine.xls"
    val normalSpreadSheet = topDir + fileSep + "ConstituentsExport.xls"
    val multiSheetSpreadSheet = topDir + fileSep + "MultiSheet.xls"
    val multiSheetSpreadSheetWihtEmpty = topDir + fileSep + "MultiSheetWithEmpty.xls"
    val simpleSpreadSheet = topDir + fileSep + "Simple.xls"
    val escapeSpreadSheet = topDir + fileSep + "EscapeEmbedded.xls"
    val converter = POIXLStoCSV
  }

  it should "extract data from a simple one row spreadsheet with 3 cells" in new Values {
    //cancel("disabled for now")
    val result: Vector[Vector[String]] = converter.convert(new FileInputStream(simpleSpreadSheet))

    assert(!result.isEmpty)
    assert(result.size == 1)

    assert(result.head.size == 3)
  }

  it should "extract data(including empty rows) from a simple seven row spreadsheet" in new Values {
    val result: Vector[Vector[String]] = converter.convert(new FileInputStream(multiLineSpreadSheet))

    assert(!result.isEmpty)
    assert(result.size == 7)

    assert(result(0).size == 0)
    assert(result(1).size == 0)
    assert(result(2).size == 3)
    assert(result(3).size == 0)
    assert(result(4).size == 3)
    assert(result(5).size == 0)
    assert(result(6).size == 4)
  }

  it should "gracefully cope with an empty file" in new Values {
    //cancel("disabled for now")
    val result: Vector[Vector[String]] = converter.convert(new FileInputStream(emptySpreadSheet))

    assert(result.isEmpty)
  }

  it should "throw an exception for an illegal data stream" in new Values {
    //cancel("disabled for now")
    a[IllegalArgumentException] should be thrownBy {
      converter.convert(new StringBufferInputStream(""))
    }
  }

  it should "extract one row of data from a multi-sheet spreadsheet" in new Values {
    //cancel("disabled for now")
    val result: Vector[Vector[String]] = converter.convert(new FileInputStream(onelineSpreadSheet))

    assert(!result.isEmpty)
  }

  it should "extract 15 rows of data(including empty rows) from a multi-sheet spreadsheet" in new Values {
    //cancel("disabled for now")
    val result: Vector[Vector[String]] = converter.convert(new FileInputStream(multiSheetSpreadSheet))

    assert(!result.isEmpty)
    assert(result.size == 15)
  }

  it should "extract 15 rows of data(including empty rows) from a multi-sheet spreadsheet with empty sheets" in new Values {
    //cancel("disabled for now")
    val result: Vector[Vector[String]] = converter.convert(new FileInputStream(multiSheetSpreadSheetWihtEmpty))

    assert(!result.isEmpty)
    assert(result.size == 15)
  }

  it should "extract data from an average spreadsheet" in new Values {
    //cancel("disabled for now")
    val result: Vector[Vector[String]] = converter.convert(new FileInputStream(normalSpreadSheet))

    assert(!result.isEmpty)
  }

  it should "extract data with commas and quotes" in new Values {
    //cancel("disabled for now")
    val result: Vector[Vector[String]] = converter.convert(new FileInputStream(escapeSpreadSheet))

    assert(!result.isEmpty)
    assert(result.size == 1)

    val row = result(0)
    var cell = row(0)
    assert(cell == "2,300")
    cell = row(1)
    assert(cell == "How's that?")
    cell = row(2)
    assert(cell == "This is a \"good\" quote.")
    cell = row(3)
    assert(cell == "We've seen \"the light\", and it was good.")
    cell = row(4)
    assert(cell == "This goes over\ntwo lines.")
  }
}