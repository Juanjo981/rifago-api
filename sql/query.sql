-- =========================================
-- SCHEMA
-- =========================================
CREATE SCHEMA IF NOT EXISTS rifago;
SET search_path TO rifago;

-- =========================================
-- ADMIN USER
-- =========================================
CREATE TABLE admin_user (
                            id BIGSERIAL PRIMARY KEY,
                            nombre VARCHAR(100) NOT NULL,
                            telefono VARCHAR(20),
                            email VARCHAR(150),
                            username VARCHAR(100) NOT NULL UNIQUE,
                            password_hash VARCHAR(255) NOT NULL,
                            activo BOOLEAN NOT NULL DEFAULT TRUE,
                            created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- =========================================
-- RIFA
-- =========================================
CREATE TABLE rifa (
                      id BIGSERIAL PRIMARY KEY,
                      codigo VARCHAR(50) NOT NULL UNIQUE,
                      titulo VARCHAR(150) NOT NULL,
                      descripcion TEXT,
                      created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                      fecha_rifa TIMESTAMP NOT NULL,
                      estado VARCHAR(20) NOT NULL DEFAULT 'BORRADOR',
                      admin_id BIGINT NOT NULL,
                      CONSTRAINT fk_rifa_admin
                          FOREIGN KEY (admin_id)
                              REFERENCES admin_user(id)
);

-- =========================================
-- PREMIO
-- =========================================
CREATE TABLE premio (
                        id BIGSERIAL PRIMARY KEY,
                        rifa_id BIGINT NOT NULL,
                        nombre VARCHAR(150) NOT NULL,
                        descripcion TEXT,
                        precio NUMERIC(12,2) NOT NULL,
                        foto_url TEXT,
                        ganador_participante_id BIGINT,
                        orden INTEGER,
                        CONSTRAINT fk_premio_rifa
                            FOREIGN KEY (rifa_id)
                                REFERENCES rifa(id),
                        CONSTRAINT fk_premio_ganador
                            FOREIGN KEY (ganador_participante_id)
                                REFERENCES participante(id),
                        CONSTRAINT uq_premio_rifa_nombre
                            UNIQUE (rifa_id, nombre)
);

-- =========================================
-- PARTICIPANTE
-- =========================================
CREATE TABLE participante (
                              id BIGSERIAL PRIMARY KEY,
                              rifa_id BIGINT NOT NULL,
                              nombre VARCHAR(150) NOT NULL,
                              telefono VARCHAR(20) NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                              CONSTRAINT fk_participante_rifa
                                  FOREIGN KEY (rifa_id)
                                      REFERENCES rifa(id),
                              CONSTRAINT uq_participante_rifa_telefono
                                  UNIQUE (rifa_id, telefono)
);

-- =========================================
-- SORTEO SESION
-- =========================================
CREATE TABLE sorteo_sesion (
                               id BIGSERIAL PRIMARY KEY,
                               rifa_id BIGINT NOT NULL,
                               started_at TIMESTAMP NOT NULL,
                               ended_at TIMESTAMP,
                               estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVA',
                               created_by_admin_id BIGINT NOT NULL,
                               CONSTRAINT fk_sorteo_sesion_rifa
                                   FOREIGN KEY (rifa_id)
                                       REFERENCES rifa(id),
                               CONSTRAINT fk_sorteo_sesion_admin
                                   FOREIGN KEY (created_by_admin_id)
                                       REFERENCES admin_user(id),
                               CONSTRAINT uq_sorteo_sesion_rifa
                                   UNIQUE (rifa_id)
);

-- =========================================
-- SORTEO EVENTO (AUDITORÍA)
-- =========================================
CREATE TABLE sorteo_evento (
                               id BIGSERIAL PRIMARY KEY,
                               sorteo_sesion_id BIGINT NOT NULL,
                               premio_id BIGINT NOT NULL,
                               participante_id BIGINT NOT NULL,
                               indice_salida INTEGER NOT NULL,
                               es_ganador BOOLEAN NOT NULL DEFAULT FALSE,
                               created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                               CONSTRAINT fk_evento_sesion
                                   FOREIGN KEY (sorteo_sesion_id)
                                       REFERENCES sorteo_sesion(id),
                               CONSTRAINT fk_evento_premio
                                   FOREIGN KEY (premio_id)
                                       REFERENCES premio(id),
                               CONSTRAINT fk_evento_participante
                                   FOREIGN KEY (participante_id)
                                       REFERENCES participante(id),
                               CONSTRAINT uq_evento_salida
                                   UNIQUE (sorteo_sesion_id, premio_id, indice_salida)
);

-- =========================================
-- ÍNDICES RECOMENDADOS
-- =========================================
CREATE INDEX idx_participante_rifa
    ON participante (rifa_id);

CREATE INDEX idx_premio_rifa
    ON premio (rifa_id);

CREATE INDEX idx_sorteo_evento_sesion
    ON sorteo_evento (sorteo_sesion_id);

CREATE INDEX idx_sorteo_evento_premio
    ON sorteo_evento (premio_id);
