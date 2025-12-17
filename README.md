==================================================
FLUJO COMPLETO PARA PROBAR RIFA CON VARIOS PREMIOS
RifaGo – Backend API
==================================================

REQUISITO PREVIO
- Tener la API corriendo
- Tener PostgreSQL levantado
- Usar Postman o similar
- Usar token JWT en endpoints admin

--------------------------------------------------
1) LOGIN ADMIN
--------------------------------------------------
POST /api/admin/auth/login

Guardar el token y usarlo en todos los endpoints admin:

Header:
Authorization: Bearer {token}

--------------------------------------------------
2) CREAR RIFA
--------------------------------------------------
POST /api/admin/rifas

Estado inicial: BORRADOR

Guardar:
- rifaId
- codigo de la rifa

--------------------------------------------------
3) CREAR PREMIOS (VARIOS)
--------------------------------------------------
POST /api/admin/rifas/{rifaId}/premios

Crear varios premios con precios distintos.
El orden del sorteo será por precio ASC.

Ejemplo:
- Premio A: $500
- Premio B: $1000
- Premio C: $5000

Guardar todos los premioId.

--------------------------------------------------
4) ABRIR RIFA
--------------------------------------------------
POST /api/admin/rifas/{rifaId}/abrir

Estado:
BORRADOR -> ABIERTA

--------------------------------------------------
5) REGISTRAR PARTICIPANTES (PÚBLICO)
--------------------------------------------------
POST /api/public/rifas/{codigo}/participantes

Registrar varios participantes.
Reglas:
- Teléfono único por rifa
- Solo permitido si estado = ABIERTA

Registrar más participantes que premios.

--------------------------------------------------
6) INICIAR SORTEO
--------------------------------------------------
POST /api/admin/rifas/{rifaId}/sorteo/iniciar

Esto hace:
- Crea sorteo_sesion
- Cambia rifa a EN_SORTEO
- Fija orden de premios por precio ASC

Guardar:
- sesionId

--------------------------------------------------
7) CONFIGURAR PREMIO 1
--------------------------------------------------
POST /api/admin/sorteo/{sesionId}/premio/{premioId1}/config

Body ejemplo:
salidas = 4
ganadorEn = 3

--------------------------------------------------
8) REVELAR PARTICIPANTES PREMIO 1
--------------------------------------------------
POST /api/admin/sorteo/{sesionId}/premio/{premioId1}/siguiente

Repetir este endpoint varias veces.
Cada llamada revela un participante.

Cuando:
esGanador = true

Entonces:
- El premio queda cerrado
- El ganador se asigna al premio
- Ese participante queda excluido de premios siguientes

--------------------------------------------------
9) CONFIGURAR PREMIO 2
--------------------------------------------------
POST /api/admin/sorteo/{sesionId}/premio/{premioId2}/config

--------------------------------------------------
10) REVELAR PARTICIPANTES PREMIO 2
--------------------------------------------------
POST /api/admin/sorteo/{sesionId}/premio/{premioId2}/siguiente

Repetir hasta que:
esGanador = true

El ganador NO puede ser el mismo que el premio anterior.

--------------------------------------------------
11) CONFIGURAR PREMIO 3
--------------------------------------------------
POST /api/admin/sorteo/{sesionId}/premio/{premioId3}/config

--------------------------------------------------
12) REVELAR PARTICIPANTES PREMIO 3
--------------------------------------------------
POST /api/admin/sorteo/{sesionId}/premio/{premioId3}/siguiente

Repetir hasta que:
esGanador = true

--------------------------------------------------
13) FINALIZAR SORTEO
--------------------------------------------------
POST /api/admin/rifas/{rifaId}/sorteo/finalizar

Estado:
EN_SORTEO -> FINALIZADA

--------------------------------------------------
14) VER RESUMEN PÚBLICO
--------------------------------------------------
GET /api/public/rifas/{codigo}/resumen

Muestra:
- premios
- ganadores
- sin teléfonos

--------------------------------------------------
15) VER RESUMEN ADMIN
--------------------------------------------------
GET /api/admin/rifas/{rifaId}/resumen

Muestra:
- premios
- ganadores
- teléfonos
- historial completo del sorteo

--------------------------------------------------
REGLAS IMPORTANTES
--------------------------------------------------
- Un participante no puede ganar más de un premio
- Un participante no puede salir dos veces en el mismo premio
- No se puede llamar /siguiente si el premio ya tiene ganador
- No existe endpoint "siguiente premio"
- El frontend controla cuándo pasar al siguiente premio
- El backend solo garantiza la integridad del sorteo

--------------------------------------------------
ESTADOS DE LA RIFA
--------------------------------------------------
BORRADOR -> ABIERTA -> EN_SORTEO -> FINALIZADA

--------------------------------------------------
NOTA FINAL
--------------------------------------------------
Este flujo debe seguirse EXACTAMENTE en este orden
para probar rifas con múltiples premios sin errores.

==================================================
FIN DEL ARCHIVO
==================================================
