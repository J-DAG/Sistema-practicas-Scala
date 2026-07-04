package modelo.reportes

import modelo.entidades.{EstudianteResumen, PracticaCoordinadorResumen}

final case class SegmentoReporte(
    clave: String,
    nombre: String,
    cantidad: Int,
    total: Int
) {
  val porcentaje: Int =
    if (total == 0) 0 else Math.round((cantidad.toDouble / total.toDouble) * 100).toInt

  def texto: String = s"$nombre: $cantidad ($porcentaje%)"
}

final case class ResumenCiclo(
    ciclo: Option[Int],
    enPractica: Int,
    finalizadas: Int,
    completadas: Int,
    postulando: Int,
    sinActividad: Int
) {
  val nombre: String = ciclo.map(numero => s"Ciclo $numero").getOrElse("Sin ciclo")
  val total: Int = enPractica + finalizadas + completadas + postulando + sinActividad

  def texto: String =
    s"$nombre | En practica: $enPractica | Finalizadas: $finalizadas | Completadas: $completadas | Postulando: $postulando | Sin actividad: $sinActividad"
}

object AnalisisReportes {
  val EstadosPractica: List[(String, String)] =
    List(
      "activa" -> "Activas",
      "finalizada" -> "Finalizadas",
      "completada" -> "Completadas",
      "cerrada" -> "Cerradas"
    )

  val EstadosEstudiante: List[(String, String)] =
    List(
      "En practica" -> "En practica",
      "Finalizada" -> "Finalizada",
      "Completada" -> "Completada",
      "Postulando" -> "Postulando",
      "Sin actividad" -> "Sin actividad"
    )

  def resumenPracticas(practicas: List[PracticaCoordinadorResumen]): List[SegmentoReporte] =
    resumenPracticasDesdeConteos(
      practicas.groupMapReduce(practica => normalizar(practica.estado))(_ => 1)(_ + _)
    )

  def resumenPracticasDesdeConteos(conteos: Map[String, Int]): List[SegmentoReporte] = {
    val total = EstadosPractica.map { case (estado, _) => conteos.getOrElse(estado, 0) }.sum
    EstadosPractica.map { case (estado, nombre) =>
      SegmentoReporte(estado, nombre, conteos.getOrElse(estado, 0), total)
    }
  }

  def resumenEstudiantes(estudiantes: List[EstudianteResumen]): List[SegmentoReporte] = {
    val conteos = estudiantes.groupMapReduce(_.estadoPractica)(_ => 1)(_ + _)
    val total = estudiantes.size

    EstadosEstudiante.map { case (estado, nombre) =>
      SegmentoReporte(estado, nombre, conteos.getOrElse(estado, 0), total)
    }
  }

  def resumenCiclos(estudiantes: List[EstudianteResumen], minimoCiclos: Int = 10): List[ResumenCiclo] = {
    val estudiantesPorCiclo = estudiantes.groupBy(_.cicloActual)
    val ciclosRegistrados = estudiantesPorCiclo.keys.flatten.toList
    val ultimoCiclo = math.max(minimoCiclos, ciclosRegistrados.foldLeft(minimoCiclos)(math.max))

    val ciclosConNumero =
      (1 to ultimoCiclo).toList.map(ciclo => resumenCiclo(Some(ciclo), estudiantesPorCiclo.getOrElse(Some(ciclo), Nil)))

    val cicloSinAsignar = estudiantesPorCiclo
      .get(None)
      .filter(_.nonEmpty)
      .map(estudiantesSinCiclo => resumenCiclo(None, estudiantesSinCiclo))
      .toList

    ciclosConNumero ++ cicloSinAsignar
  }

  private def resumenCiclo(ciclo: Option[Int], estudiantes: List[EstudianteResumen]): ResumenCiclo = {
    val conteos = estudiantes.groupMapReduce(_.estadoPractica)(_ => 1)(_ + _)
    ResumenCiclo(
      ciclo = ciclo,
      enPractica = conteos.getOrElse("En practica", 0),
      finalizadas = conteos.getOrElse("Finalizada", 0),
      completadas = conteos.getOrElse("Completada", 0),
      postulando = conteos.getOrElse("Postulando", 0),
      sinActividad = conteos.getOrElse("Sin actividad", 0)
    )
  }

  private def normalizar(valor: String): String =
    valor.trim.toLowerCase
}
