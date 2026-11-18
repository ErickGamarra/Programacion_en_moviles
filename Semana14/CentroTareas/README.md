# CentroTareas

App Android en Kotlin para practicar hilos y corrutinas. Tiene una tarea con barra de progreso, un cálculo pesado (contar números primos) y dos cargas de datos simuladas. La idea es que la pantalla nunca se congele.

Laboratorio 14 de Programación en Móviles (Tecsup) · Hilos en Android · Erick Gamarra · trabajo individual

## Observación del Paso 1

Al tocar **Tarea larga (MAL)** la pantalla se queda congelada 4 segundos: el botón Tócame no suma y nada responde. Cuando termina, el texto pasa directo de Listo a Terminó y los toques que hice se procesan de golpe.

El texto **Trabajando...** nunca se ve porque `Thread.sleep(4000)` corre en el hilo principal, que es el mismo que dibuja la pantalla. Cambio el mensaje, pero el hilo se duerme antes de poder redibujar, y cuando despierta ya lo cambié a Terminó.

Si la espera fuera de 10 segundos, Android mostraría el aviso **"La aplicación no responde" (ANR)**, con la opción de cerrarla o esperar. Ese aviso sale cuando el hilo principal no atiende un toque en unos 5 segundos.
