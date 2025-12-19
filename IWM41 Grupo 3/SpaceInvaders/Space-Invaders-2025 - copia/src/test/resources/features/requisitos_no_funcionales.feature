Feature: Requisitos No Funcionales del Sistema Space Invaders
  Para asegurar la calidad del producto
  Como usuario
  Quiero verificar el comportamiento de la ventana y los recursos

  Scenario: Redimensionado de ventana proporcional
    Given el juego esta iniciado en modo ventana
    When el usuario cambia el tamaño de la ventana a 800x600
    Then los componentes del juego deben reescalarse proporcionalmente
    And el area de juego debe ocupar todo el panel visible

  Scenario: Verificación de recursos gráficos
    Given el tablero de juego ha sido inicializado
    When verifico la carga de las imagenes de los sprites
    Then la imagen del "Player" no debe ser nula
    And la imagen del "Alien" no debe ser nula
    And la imagen del "Shot" no debe ser nula

  Scenario: Cierre de la aplicación
    Given el juego se esta ejecutando
    When el usuario hace click en el boton de cerrar ventana
    Then el proceso de la aplicacion debe finalizar completamente

  Scenario: Instancia única
    Given ya existe una instancia del juego ejecutandose
    When intento iniciar una segunda instancia
    Then la segunda instancia no debe iniciarse