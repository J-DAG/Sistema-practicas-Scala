package controlador.util

import modelo.entidades.{PostulacionResumen, PracticaCoordinadorResumen, TutorResumen, Usuario}

import javax.swing.table.DefaultTableModel

object ModelosTabla {
  def usuarios(datos: List[Usuario]): DefaultTableModel =
    modeloNoEditable(
      columnas = Array[AnyRef]("ID", "Nombres", "Apellidos", "Cedula", "Email", "Rol", "Activo"),
      filas = datos.map(usuario =>
        Array[AnyRef](
          Long.box(usuario.idUsuario),
          usuario.nombres,
          usuario.apellidos,
          usuario.cedula,
          usuario.email,
          usuario.rol,
          Boolean.box(usuario.activo)
        )
      )
    )

  def tutores(datos: List[TutorResumen]): DefaultTableModel =
    modeloNoEditable(
      columnas = Array[AnyRef]("ID", "Nombres", "Apellidos", "Cedula", "Email", "Rol", "Carrera", "Empresa", "Cargo", "Activo"),
      filas = datos.map(tutor =>
        Array[AnyRef](
          Long.box(tutor.idUsuario),
          tutor.nombres,
          tutor.apellidos,
          tutor.cedula,
          tutor.email,
          tutor.rol,
          tutor.carrera.getOrElse(""),
          tutor.empresa.getOrElse(""),
          tutor.cargo.getOrElse(""),
          Boolean.box(tutor.activo)
        )
      )
    )

  def postulaciones(datos: List[PostulacionResumen]): DefaultTableModel =
    modeloNoEditable(
      columnas = Array[AnyRef]("ID", "Estudiante", "Cedula", "Empresa", "Oferta", "Area", "Fecha", "Estado", "Documento"),
      filas = datos.map(postulacion =>
        Array[AnyRef](
          Long.box(postulacion.idPostulacion),
          postulacion.estudiante,
          postulacion.cedula,
          postulacion.empresa,
          postulacion.oferta,
          postulacion.area,
          postulacion.fechaPostulacion.toString,
          postulacion.estado,
          postulacion.rutaDocumentoMalla.getOrElse("")
        )
      )
    )

  def practicasCoordinador(datos: List[PracticaCoordinadorResumen]): DefaultTableModel =
    modeloNoEditable(
      columnas = Array[AnyRef]("ID", "Estudiante", "Cedula", "Empresa", "Oferta", "Area", "TA", "TE", "Estado", "Horas", "Calificacion"),
      filas = datos.map(practica =>
        Array[AnyRef](
          Long.box(practica.idPractica),
          practica.estudiante,
          practica.cedula,
          practica.empresa,
          practica.oferta,
          practica.area,
          practica.tutorAcademico,
          practica.tutorEmpresarial,
          practica.estado,
          s"${practica.horasCumplidas}/240 (${practica.porcentaje}%)",
          practica.calificacion.map(c => s"$c/100").getOrElse("")
        )
      )
    )

  private def modeloNoEditable(columnas: Array[AnyRef], filas: List[Array[AnyRef]]): DefaultTableModel = {
    val modelo = new DefaultTableModel(columnas, 0) {
      override def isCellEditable(row: Int, column: Int): Boolean = false
    }

    filas.foldLeft(modelo) { case (tabla, fila) =>
      tabla.addRow(fila)
      tabla
    }
  }
}
