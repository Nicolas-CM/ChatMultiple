# ChatMultiple

### ¡Bienvenido al Chat Múltiple!  

En este proyecto, hemos implementado un chat que permite a los usuarios realizar diversas acciones de comunicación, incluyendo la creación de grupos de chat, envío de mensajes de texto y notas de voz a usuarios específicos o grupos, así como realizar llamadas a usuarios individuales o grupos.  

#### Características principales: 
1. Creación de grupos de chat.
2. Envío de mensajes de texto a usuarios específicos o grupos.
3. Envío de notas de voz a usuarios específicos o grupos.
4. Realización de llamadas a usuarios individuales o grupos.
5. Historial de mensajes guardado (texto y audios).

Para cada requerimiento, hemos seleccionado el protocolo de comunicación (TCP o UDP) más apropiado para garantizar una experiencia fluida y segura para los usuarios. La interacción puede realizarse mediante una interfaz de línea de comandos o una interfaz gráfica de usuario (GUI), según la preferencia del usuario.  

Integrantes del Grupo: 
- Nicolás Cuéllar Molina
- Ándres Felipe Cabezas Guerrero
- Miguel Angel Martinez Vidal
- Davide Flamini Cazaran

Para ejecutar el programa, por favor sigue las instrucciones detalladas a continuación. ¡Esperamos que disfrutes utilizando nuestro chat!

## Instrucciones

1. Para compilar el projecto, primero, debe ubicarse en su powershell en la carpeta src
2. Después, debe ejectuar el comando de acontinuación: javac *.java
3. Posteriormente, debe ejecutar el comando java Server
4. Ahora, para crear un usuario del chat, debe realizar los pasos 1 y 2 en otro powershell
5. Luego, debe ejecutar el comando java Client
6. Realizar el paso 4 y 5 según el número de usuarios que desee crear
7. Para comenzar se ingresa un nombre de usuario que debe ser aceptado si no se repite
8. Luego debe elegir si va a usar el chat de manera grupal o individual
9. Para poder escribir, enviar audios o llamar se debe crear un grupo o un chat, o unirse a uno a alguno ya existente
10. Para realizar cualquier función, debe digitar el número que aparece antes de ella en el menú
11. Por ejemplo, en el siguiente menú del grupo:
Seleccione una opcion:
 0) Salir del menu
 1) Crear un nuevo grupo
 2) Ingresar a un grupo
 3) Escribir a un grupo
 4) Ver miembros de un grupo

Para poder realizar la función de escribir, escuchar un audio, llamar o ver el historial de mensajes, debe digitar el número "3" que corresponde a "Escribir a un grupo".
En lo que se despliega el siguiente menú;
Seleccione una opcion:
 0) Salir del menu
 1) Mas Mensajes
 2) Escuchar un audio
 3) Enviar Mensajes / Llamar
-------------------
Para enviar un mensaje, audio o llamar se presiona la opción 3 y se despliega el siguiente menú:
MENU
----------
Menu Enviar
----------
 Seleccione una opcion de lo que dese enviar:
 0) Salir del menu
 1) Mensaje
 2) Audio
 3) Hacer Llamada
-------------------
Hay un caso particular y es el de la llamada ya que cuando alguien realice una llamada se recibirá un mensaje indicando quien está llamando y desde que grupo
Entonces cuando se reciba el mensaje se debe empezar a navegar por los menus hasta llegar a la opción de hacer llamada en el menú anterior para poder contestar

12. Esta misma lógica se aplica para los chats privados
13. Para poder devolverse a un menú anterior, como se indica en este último, debe digitar el número "0" que corresponde a "Salir del menú"
14. Si desea salir del chat, debe devolverse hasta el primer menú y volver a digitar el número "0"

### Importante:

15. Si desea conectarse entre diferentes computadores, deben estar conectados bajo la misma red
16. Una vez estén conectados en la misma red y antes de compilar el programa, deben utilizar el comando: "ipconfig" en su powershell
17. Después de esto, saldrá lo siguiente:
Configuración IP de Windows

Adaptador de LAN inalámbrica Wi-Fi:

   Sufijo DNS específico para la conexión. . :

   Vínculo: dirección IPv6 local. . . : fe80::91d9:610d:716d:f08f%5

   Dirección IPv4. . . . . . . . . . . . . . : 192.168.93.242

   Máscara de subred . . . . . . . . . . . . : 255.255.255.0

   Puerta de enlace predeterminada . . . . . : 192.168.93.103


18. Se debe copiar la dirección IPV4, que en este caso es: "192.168.93.242" y pegarla en la linea de código número 12 de la clase Client, más detalladamente en la variable: "SERVER_IP"
19. Posterior a ello, ya pueden realizar el paso 1.
