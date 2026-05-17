@"
# Sistema de Gestión Integral para RRHH - AP2

Alumno: Rodrigo A. Quinteros  
Materia: INF275 - Seminario de Práctica de Informática  
Entrega: AP2 - Segunda Entrega  

## Descripción

Repositorio correspondiente al prototipo operacional del Sistema de Gestión Integral para RRHH del Ministerio de Gobierno, Infraestructura y Desarrollo Territorial del Gobierno de Mendoza.

El prototipo se encuentra acotado a los módulos:

- Gestión de Agentes
- Gestión de Legajos
- Gestión de Licencias

## Tecnologías utilizadas

- Java Desktop
- Swing
- MySQL / MariaDB
- XAMPP
- phpMyAdmin
- JDBC
- NetBeans

## Estructura del repositorio

- capturas: imágenes utilizadas en el informe.
- database: archivo SQL de creación, inserción, consulta y borrado.
- diagramas: diagramas del sistema y de base de datos.
- documentacion: informe final en formato DOCX y PDF.
- librerias: dependencias utilizadas por el prototipo.
- proyecto-java: código fuente del prototipo Java.

## Base de datos

Nombre de la base de datos:

rrhh_ministerio

Archivo SQL:

database/rrhh_ministerio.sql

## Usuario de prueba

Usuario: admin  
Contraseña: admin123  

## Alcance del prototipo

El prototipo no representa la versión final del sistema, sino una implementación operacional acotada para validar el flujo principal del AP2:

Registro de agente → Consulta de agente → Registro de licencia asociada.
"@ | Out-File -Encoding UTF8 README.md