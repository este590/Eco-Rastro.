# ECO-RASTRO

Aplicación Android educativa para promover hábitos ambientales, reciclaje y ahorro de recursos.

## Funciones incluidas
- Pantalla de inicio con resumen de puntos ECO.
- Registro diario de consumo aproximado de agua, energía y residuos.
- Guía básica para separar papel/cartón, plásticos/metales, vidrio, orgánicos y no reciclables.
- Puntos ECO y niveles por registrar hábitos.
- Muro comunitario local para guardar una publicación.
- Icono de aplicación ECO-RASTRO incluido en recursos Android.

## Generar el APK desde GitHub usando un teléfono
1. Descarga y descomprime `ECO-RASTRO-GitHub.zip`.
2. En GitHub crea un repositorio llamado `ECO-RASTRO`.
3. Sube el contenido de esta carpeta (no la carpeta ZIP dentro del repositorio).
4. Abre la pestaña **Actions**. El flujo **Crear APK ECO-RASTRO** se ejecutará al subir a `main`/`master`, o puedes ejecutarlo manualmente con **Run workflow**.
5. Cuando termine correctamente, abre la ejecución y descarga el artefacto **ECO-RASTRO-APK**.
6. Dentro encontrarás `app-debug.apk`. Descárgalo e instálalo en tu Android.

## Requisitos técnicos
- Android Studio/Gradle: Android Gradle Plugin 8.7.3 y Gradle 8.9.
- Java 17.
- `minSdk 23` (Android 6.0 o posterior).

## Nota
Esta es una primera versión funcional. Los datos se guardan localmente en el dispositivo; la guía de centros de acopio es informativa y no contiene ubicaciones verificadas en tiempo real.
