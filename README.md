# 📚 SGAAC – Sistema de Gestión de Ayudantías de Cátedra

## 📖 Descripción

El **SGAAC** es un sistema académico que permite gestionar el **proceso de Ayudantías de Cátedra** en la Universidad Técnica Estatal de Quevedo (UTEQ).  
Incluye módulos para convocatorias, postulaciones, evaluaciones de méritos y pruebas de oposición, siguiendo el **Reglamento Oficial de Ayudantías de Cátedra**.

## ✨ Características principales

- 🔑 **Autenticación y roles**: Admin, Coordinador, Decano, Docente, Estudiante, Ayudante.  
- 📢 **Gestión de convocatorias**: creación, publicación y control de requisitos.  
- 📝 **Evaluación de méritos y oposición** con tribunal asignado automáticamente.  
- 📊 **Reportes y auditorías**: registro histórico de actividades.  
- 📧 **Notificaciones por correo** para postulantes y ayudantes seleccionados.  
- 🌐 Interfaz web adaptable con **Spring Boot + Thymeleaf + Tailwind CSS**.

- ## 📂 Estructura del proyecto

```plaintext
SGAAC/
├─ backend/                # Código fuente Spring Boot
│  ├─ src/main/java/...    # Lógica del negocio
│  ├─ src/main/resources/  # application.properties y plantillas
│  └─ pom.xml              # Dependencias Maven
│
├─ frontend/               # (Opcional) Interfaz en React/Vue
│
├─ docs/                   # Documentación del proyecto
│
└─ README.md               # Este archivo
