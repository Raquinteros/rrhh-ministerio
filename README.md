# Sistema de Gestión Integral para RRHH

<p align="center">
  <b>Prototipo Java de escritorio para gestión básica de recursos humanos</b><br>
  Proyecto académico desarrollado para <b>Seminario de Práctica de Informática - INF275</b>
</p>

---

## Sobre el proyecto

Este repositorio contiene un prototipo académico desarrollado en **Java** para la gestión básica de información de recursos humanos en la Oficina de Personal del Ministerio de Gobierno, Infraestructura y Desarrollo Territorial del Gobierno de Mendoza.

El proyecto forma parte de una entrega incremental de la materia **Seminario de Práctica de Informática**. La etapa actual corresponde a **AP3**, centrada en la implementación del prototipo en Java, con interfaz gráfica, clases del dominio, validaciones y evidencias de funcionamiento.

---

## Datos académicos

| Dato        | Información                                   |
| ----------- | --------------------------------------------- |
| Alumno      | Rodrigo A. Quinteros                          |
| Carrera     | Licenciatura en Informática                   |
| Universidad | Universidad Siglo 21                          |
| Materia     | Seminario de Práctica de Informática - INF275 |
| Proyecto    | Sistema de Gestión Integral para RRHH         |
| Etapa       | AP3 - Desarrollo del prototipo Java           |

---

## Tecnologías utilizadas

<p align="left">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge" />
  <img src="https://img.shields.io/badge/NetBeans-1B6AC6?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white" />
  <img src="https://img.shields.io/badge/Ant-Apache-red?style=for-the-badge&logo=apache&logoColor=white" />
  <img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white" />
  <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white" />
</p>

---

## Funcionalidades principales

* Inicio de sesión mediante ventana de login.
* Menú principal de navegación.
* Gestión básica de agentes.
* Registro y listado de agentes.
* Búsqueda por DNI.
* Gestión inicial de licencias.
* Validación de datos obligatorios.
* Manejo de errores mediante mensajes al usuario.
* Persistencia en memoria durante la ejecución.
* Base técnica preparada para conexión con MariaDB.

---

## Estructura del repositorio

```text
.
├── capturas/            # Evidencias del sistema funcionando
├── database/            # Archivos relacionados con base de datos
├── diagramas/           # Diagramas de clases y vistas del prototipo
├── documentacion/       # Documentación formal de la entrega
├── librerias/           # Librerías externas utilizadas
├── proyecto-java/       # Proyecto Java desarrollado en NetBeans
│   └── RRHH_Ministerio/
│       ├── src/         # Código fuente Java
│       ├── lib/         # Librerías del proyecto
│       ├── dist/        # JAR ejecutable generado
│       ├── nbproject/   # Configuración NetBeans/Ant
│       ├── build.xml    # Script de compilación Ant
│       └── manifest.mf  # Manifiesto del proyecto
├── README.md
├── .gitignore
└── .gitattributes
```

---

## Organización del código Java

```text
src/rrhh/
├── Main.java
├── datos/
│   ├── ConexionBD.java
│   └── RepositorioMemoria.java
├── modelo/
│   ├── Persona.java
│   └── Agente.java
└── vista/
    ├── LoginView.java
    ├── MenuPrincipalView.java
    ├── AgenteView.java
    └── LicenciaView.java
```

| Carpeta / paquete | Contenido                                               |
| ----------------- | ------------------------------------------------------- |
| `rrhh`            | Clase principal de inicio del sistema.                  |
| `rrhh.modelo`     | Clases del dominio, como `Persona` y `Agente`.          |
| `rrhh.datos`      | Acceso a datos, repositorio en memoria y conexión a BD. |
| `rrhh.vista`      | Ventanas gráficas desarrolladas con Java Swing.         |

---

## Cómo ejecutar el proyecto

### Desde NetBeans

1. Abrir NetBeans.
2. Seleccionar **Open Project**.
3. Abrir la carpeta:

```text
proyecto-java/RRHH_Ministerio
```

4. Ejecutar el proyecto con **Run** o `F6`.

### Desde el archivo JAR

```cmd
java -jar "proyecto-java\RRHH_Ministerio\dist\RRHH_Ministerio.jar"
```

---

## Evidencias y documentación

| Carpeta          | Contenido                                                     |
| ---------------- | ------------------------------------------------------------- |
| `capturas/`      | Capturas del sistema funcionando y evidencias de compilación. |
| `diagramas/`     | Diagramas utilizados para documentar el diseño del prototipo. |
| `documentacion/` | PDF formal de la entrega AP3.                                 |
| `database/`      | Archivos relacionados con la base de datos.                   |

Documento principal:

```text
documentacion/QUINTEROS-RODRIGO-AP3.pdf
```

---

## Estado actual

```text
AP3 finalizado - Prototipo Java funcional
```

El repositorio incluye código fuente, ejecutable JAR, capturas, diagramas, documentación formal y archivos de apoyo para base de datos.

---

## Uso

Repositorio de uso académico. El sistema representa un prototipo en desarrollo y no debe considerarse una versión productiva final.
