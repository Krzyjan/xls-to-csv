package org.krzyjan.utilities

import java.io.InputStream

trait XLStoCSV {
  def convert(in: InputStream): Vector[Vector[String]]

  def generateRows(in: InputStream, separator: String): Vector[String] = {

    def escapeEmbeddedCharacters(cell: String): String = {
      if (cell.contains("\"")) {
        s""""${cell.replace("\"", "\"\"")}""""
      } else if (cell.contains(separator) || cell.contains("\n")) {
        s""""${cell}""""
      } else {
        cell
      }
    }

    def combine(row: Vector[String]): String = {
      row.map(c => escapeEmbeddedCharacters(c)).mkString(separator)
    }

    convert(in).map(combine)
  }
}