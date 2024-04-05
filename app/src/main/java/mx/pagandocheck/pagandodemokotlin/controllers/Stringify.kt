package mx.pagandocheck.pagandodemokotlin.controllers


object Stringfy {
    fun getString(obj: Any): String {
        val resultado = StringBuilder() // Usamos StringBuilder para construir el String
        val clazz: Class<*> = obj.javaClass
        val campos = clazz.declaredFields // Obtiene todos los campos de la clase
        resultado.append(clazz.getSimpleName()).append("(")
        for (i in campos.indices) {
            campos[i].isAccessible = true
            try {
                resultado.append(campos[i].getName()).append("=").append(campos[i][obj])
                if (i < campos.size - 1) {
                    resultado.append(", ")
                }
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
        resultado.append(")")
        return resultado.toString()
    }
}

