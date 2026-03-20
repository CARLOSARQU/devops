# language: es
Característica: Login en la aplicación Los Andes

  Esquema del escenario: <testCase>
    Dado estoy en la pantalla de bienvenida
    Cuando ingreso el usuario "<usuario>" y la clave "<clave>"
    Entonces el resultado debería ser "<esperado>"

    Ejemplos:
      | testCase               | usuario  | clave  | esperado    |
      | Login Exitoso          | 71313648 | 140304 | exitoso     |
      | Credenciales Inválidas | 00000000 | 000000 | error_modal |
