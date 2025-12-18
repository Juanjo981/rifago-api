--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

-- Started on 2025-12-18 13:59:07

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 49304)
-- Name: rifago; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA rifago;


ALTER SCHEMA rifago OWNER TO postgres;

--
-- TOC entry 863 (class 1247 OID 49307)
-- Name: rifa_estado; Type: TYPE; Schema: rifago; Owner: postgres
--

CREATE TYPE rifago.rifa_estado AS ENUM (
    'BORRADOR',
    'ABIERTA',
    'EN_SORTEO',
    'FINALIZADA',
    'CANCELADA'
);


ALTER TYPE rifago.rifa_estado OWNER TO postgres;

--
-- TOC entry 234 (class 1255 OID 49456)
-- Name: fn_validar_evento_consistencia_rifa(); Type: FUNCTION; Schema: rifago; Owner: postgres
--

CREATE FUNCTION rifago.fn_validar_evento_consistencia_rifa() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM rifago.sorteo_sesion ss
        WHERE ss.id = NEW.sorteo_sesion_id
    ) THEN
        RAISE EXCEPTION 'Sorteo sesión inválida';
END IF;

RETURN NEW;
END;
$$;


ALTER FUNCTION rifago.fn_validar_evento_consistencia_rifa() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 219 (class 1259 OID 49324)
-- Name: admin_user; Type: TABLE; Schema: rifago; Owner: postgres
--

CREATE TABLE rifago.admin_user (
                                   id bigint NOT NULL,
                                   nombre character varying(120) NOT NULL,
                                   telefono character varying(30),
                                   email character varying(180),
                                   username character varying(60) NOT NULL,
                                   password_hash text NOT NULL,
                                   activo boolean DEFAULT true NOT NULL,
                                   created_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE rifago.admin_user OWNER TO postgres;

--
-- TOC entry 4962 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE admin_user; Type: COMMENT; Schema: rifago; Owner: postgres
--

COMMENT ON TABLE rifago.admin_user IS 'Admins que crean/operan rifas (login).';


--
-- TOC entry 218 (class 1259 OID 49323)
-- Name: admin_user_id_seq; Type: SEQUENCE; Schema: rifago; Owner: postgres
--

CREATE SEQUENCE rifago.admin_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE rifago.admin_user_id_seq OWNER TO postgres;

--
-- TOC entry 4963 (class 0 OID 0)
-- Dependencies: 218
-- Name: admin_user_id_seq; Type: SEQUENCE OWNED BY; Schema: rifago; Owner: postgres
--

ALTER SEQUENCE rifago.admin_user_id_seq OWNED BY rifago.admin_user.id;


--
-- TOC entry 223 (class 1259 OID 49359)
-- Name: participante; Type: TABLE; Schema: rifago; Owner: postgres
--

CREATE TABLE rifago.participante (
                                     id bigint NOT NULL,
                                     rifa_id bigint NOT NULL,
                                     nombre character varying(140) NOT NULL,
                                     telefono character varying(30) NOT NULL,
                                     created_at timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE rifago.participante OWNER TO postgres;

--
-- TOC entry 4964 (class 0 OID 0)
-- Dependencies: 223
-- Name: TABLE participante; Type: COMMENT; Schema: rifago; Owner: postgres
--

COMMENT ON TABLE rifago.participante IS 'Registro por rifa. Telefono unico por rifa.';


--
-- TOC entry 222 (class 1259 OID 49358)
-- Name: participante_id_seq; Type: SEQUENCE; Schema: rifago; Owner: postgres
--

CREATE SEQUENCE rifago.participante_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE rifago.participante_id_seq OWNER TO postgres;

--
-- TOC entry 4965 (class 0 OID 0)
-- Dependencies: 222
-- Name: participante_id_seq; Type: SEQUENCE OWNED BY; Schema: rifago; Owner: postgres
--

ALTER SEQUENCE rifago.participante_id_seq OWNED BY rifago.participante.id;


--
-- TOC entry 225 (class 1259 OID 49376)
-- Name: premio; Type: TABLE; Schema: rifago; Owner: postgres
--

CREATE TABLE rifago.premio (
                               id bigint NOT NULL,
                               rifa_id bigint NOT NULL,
                               nombre character varying(140) NOT NULL,
                               descripcion text,
                               precio numeric(12,2) NOT NULL,
                               foto_url text,
                               ganador_participante_id bigint,
                               orden integer,
                               created_at timestamp with time zone DEFAULT now() NOT NULL,
                               CONSTRAINT premio_precio_check CHECK ((precio >= (0)::numeric))
);


ALTER TABLE rifago.premio OWNER TO postgres;

--
-- TOC entry 4966 (class 0 OID 0)
-- Dependencies: 225
-- Name: TABLE premio; Type: COMMENT; Schema: rifago; Owner: postgres
--

COMMENT ON TABLE rifago.premio IS 'Premios por rifa, se ordenan por precio ASC al sortear.';


--
-- TOC entry 224 (class 1259 OID 49375)
-- Name: premio_id_seq; Type: SEQUENCE; Schema: rifago; Owner: postgres
--

CREATE SEQUENCE rifago.premio_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE rifago.premio_id_seq OWNER TO postgres;

--
-- TOC entry 4967 (class 0 OID 0)
-- Dependencies: 224
-- Name: premio_id_seq; Type: SEQUENCE OWNED BY; Schema: rifago; Owner: postgres
--

ALTER SEQUENCE rifago.premio_id_seq OWNED BY rifago.premio.id;


--
-- TOC entry 221 (class 1259 OID 49338)
-- Name: rifa; Type: TABLE; Schema: rifago; Owner: postgres
--

CREATE TABLE rifago.rifa (
                             id bigint NOT NULL,
                             codigo character varying(32) NOT NULL,
                             titulo character varying(160) NOT NULL,
                             descripcion text,
                             created_at timestamp with time zone DEFAULT now() NOT NULL,
                             fecha_rifa timestamp with time zone,
                             estado character varying NOT NULL,
                             admin_id bigint NOT NULL
);


ALTER TABLE rifago.rifa OWNER TO postgres;

--
-- TOC entry 4968 (class 0 OID 0)
-- Dependencies: 221
-- Name: TABLE rifa; Type: COMMENT; Schema: rifago; Owner: postgres
--

COMMENT ON TABLE rifago.rifa IS 'Cabecera de la rifa, codigo es el que usan participantes.';


--
-- TOC entry 220 (class 1259 OID 49337)
-- Name: rifa_id_seq; Type: SEQUENCE; Schema: rifago; Owner: postgres
--

CREATE SEQUENCE rifago.rifa_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE rifago.rifa_id_seq OWNER TO postgres;

--
-- TOC entry 4969 (class 0 OID 0)
-- Dependencies: 220
-- Name: rifa_id_seq; Type: SEQUENCE OWNED BY; Schema: rifago; Owner: postgres
--

ALTER SEQUENCE rifago.rifa_id_seq OWNED BY rifago.rifa.id;


--
-- TOC entry 227 (class 1259 OID 49426)
-- Name: sorteo_evento; Type: TABLE; Schema: rifago; Owner: postgres
--

CREATE TABLE rifago.sorteo_evento (
                                      id bigint NOT NULL,
                                      sorteo_sesion_id bigint NOT NULL,
                                      premio_id bigint NOT NULL,
                                      participante_id bigint NOT NULL,
                                      indice_salida integer NOT NULL,
                                      es_ganador boolean DEFAULT false NOT NULL,
                                      created_at timestamp with time zone DEFAULT now() NOT NULL,
                                      CONSTRAINT sorteo_evento_indice_salida_check CHECK ((indice_salida >= 1))
);


ALTER TABLE rifago.sorteo_evento OWNER TO postgres;

--
-- TOC entry 4970 (class 0 OID 0)
-- Dependencies: 227
-- Name: TABLE sorteo_evento; Type: COMMENT; Schema: rifago; Owner: postgres
--

COMMENT ON TABLE rifago.sorteo_evento IS 'Bitacora por premio: quien salio en que orden y quien gano.';


--
-- TOC entry 226 (class 1259 OID 49425)
-- Name: sorteo_evento_id_seq; Type: SEQUENCE; Schema: rifago; Owner: postgres
--

CREATE SEQUENCE rifago.sorteo_evento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE rifago.sorteo_evento_id_seq OWNER TO postgres;

--
-- TOC entry 4971 (class 0 OID 0)
-- Dependencies: 226
-- Name: sorteo_evento_id_seq; Type: SEQUENCE OWNED BY; Schema: rifago; Owner: postgres
--

ALTER SEQUENCE rifago.sorteo_evento_id_seq OWNED BY rifago.sorteo_evento.id;


--
-- TOC entry 233 (class 1259 OID 57358)
-- Name: sorteo_premio_config; Type: TABLE; Schema: rifago; Owner: postgres
--

CREATE TABLE rifago.sorteo_premio_config (
                                             id bigint NOT NULL,
                                             sorteo_sesion_id bigint NOT NULL,
                                             premio_id bigint NOT NULL,
                                             total_salidas integer NOT NULL,
                                             ganador_en integer NOT NULL,
                                             created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
                                             CONSTRAINT chk_ganador_en_valido CHECK ((ganador_en <= total_salidas)),
                                             CONSTRAINT sorteo_premio_config_ganador_en_check CHECK ((ganador_en > 0)),
                                             CONSTRAINT sorteo_premio_config_total_salidas_check CHECK ((total_salidas > 0))
);


ALTER TABLE rifago.sorteo_premio_config OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 57357)
-- Name: sorteo_premio_config_id_seq; Type: SEQUENCE; Schema: rifago; Owner: postgres
--

CREATE SEQUENCE rifago.sorteo_premio_config_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE rifago.sorteo_premio_config_id_seq OWNER TO postgres;

--
-- TOC entry 4972 (class 0 OID 0)
-- Dependencies: 232
-- Name: sorteo_premio_config_id_seq; Type: SEQUENCE OWNED BY; Schema: rifago; Owner: postgres
--

ALTER SEQUENCE rifago.sorteo_premio_config_id_seq OWNED BY rifago.sorteo_premio_config.id;


--
-- TOC entry 229 (class 1259 OID 49508)
-- Name: sorteo_sesion; Type: TABLE; Schema: rifago; Owner: postgres
--

CREATE TABLE rifago.sorteo_sesion (
                                      id bigint NOT NULL,
                                      rifa_id bigint NOT NULL,
                                      started_at timestamp without time zone NOT NULL,
                                      ended_at timestamp without time zone,
                                      estado character varying DEFAULT 'ACTIVA'::character varying NOT NULL,
                                      created_by_admin_id bigint NOT NULL
);


ALTER TABLE rifago.sorteo_sesion OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 49507)
-- Name: sorteo_sesion_id_seq; Type: SEQUENCE; Schema: rifago; Owner: postgres
--

CREATE SEQUENCE rifago.sorteo_sesion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE rifago.sorteo_sesion_id_seq OWNER TO postgres;

--
-- TOC entry 4973 (class 0 OID 0)
-- Dependencies: 228
-- Name: sorteo_sesion_id_seq; Type: SEQUENCE OWNED BY; Schema: rifago; Owner: postgres
--

ALTER SEQUENCE rifago.sorteo_sesion_id_seq OWNED BY rifago.sorteo_sesion.id;


--
-- TOC entry 230 (class 1259 OID 49527)
-- Name: vw_ganadores_por_rifa; Type: VIEW; Schema: rifago; Owner: postgres
--

CREATE VIEW rifago.vw_ganadores_por_rifa AS
SELECT r.id AS rifa_id,
       p.ganador_participante_id AS participante_id
FROM (rifago.rifa r
    JOIN rifago.premio p ON ((p.rifa_id = r.id)))
WHERE (p.ganador_participante_id IS NOT NULL);


ALTER VIEW rifago.vw_ganadores_por_rifa OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 49531)
-- Name: vw_pool_elegible; Type: VIEW; Schema: rifago; Owner: postgres
--

CREATE VIEW rifago.vw_pool_elegible AS
SELECT pr.id AS premio_id,
       ss.id AS sorteo_sesion_id,
       pa.id AS participante_id,
       pa.nombre,
       pa.telefono,
       pa.rifa_id
FROM ((((rifago.premio pr
    JOIN rifago.sorteo_sesion ss ON ((ss.rifa_id = pr.rifa_id)))
    JOIN rifago.participante pa ON ((pa.rifa_id = pr.rifa_id)))
    LEFT JOIN rifago.vw_ganadores_por_rifa g ON (((g.rifa_id = pr.rifa_id) AND (g.participante_id = pa.id))))
    LEFT JOIN rifago.sorteo_evento ev ON (((ev.sorteo_sesion_id = ss.id) AND (ev.premio_id = pr.id) AND (ev.participante_id = pa.id))))
WHERE (((ss.estado)::text = 'ACTIVA'::text) AND (g.participante_id IS NULL) AND (ev.id IS NULL));


ALTER VIEW rifago.vw_pool_elegible OWNER TO postgres;

--
-- TOC entry 4738 (class 2604 OID 49327)
-- Name: admin_user id; Type: DEFAULT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.admin_user ALTER COLUMN id SET DEFAULT nextval('rifago.admin_user_id_seq'::regclass);


--
-- TOC entry 4743 (class 2604 OID 49362)
-- Name: participante id; Type: DEFAULT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.participante ALTER COLUMN id SET DEFAULT nextval('rifago.participante_id_seq'::regclass);


--
-- TOC entry 4745 (class 2604 OID 49379)
-- Name: premio id; Type: DEFAULT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.premio ALTER COLUMN id SET DEFAULT nextval('rifago.premio_id_seq'::regclass);


--
-- TOC entry 4741 (class 2604 OID 49341)
-- Name: rifa id; Type: DEFAULT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.rifa ALTER COLUMN id SET DEFAULT nextval('rifago.rifa_id_seq'::regclass);


--
-- TOC entry 4747 (class 2604 OID 49429)
-- Name: sorteo_evento id; Type: DEFAULT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.sorteo_evento ALTER COLUMN id SET DEFAULT nextval('rifago.sorteo_evento_id_seq'::regclass);


--
-- TOC entry 4752 (class 2604 OID 57361)
-- Name: sorteo_premio_config id; Type: DEFAULT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.sorteo_premio_config ALTER COLUMN id SET DEFAULT nextval('rifago.sorteo_premio_config_id_seq'::regclass);


--
-- TOC entry 4750 (class 2604 OID 49511)
-- Name: sorteo_sesion id; Type: DEFAULT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.sorteo_sesion ALTER COLUMN id SET DEFAULT nextval('rifago.sorteo_sesion_id_seq'::regclass);


--
-- TOC entry 4760 (class 2606 OID 49333)
-- Name: admin_user admin_user_pkey; Type: CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.admin_user
    ADD CONSTRAINT admin_user_pkey PRIMARY KEY (id);


--
-- TOC entry 4762 (class 2606 OID 49335)
-- Name: admin_user admin_user_username_key; Type: CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.admin_user
    ADD CONSTRAINT admin_user_username_key UNIQUE (username);


--
-- TOC entry 4774 (class 2606 OID 49365)
-- Name: participante participante_pkey; Type: CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.participante
    ADD CONSTRAINT participante_pkey PRIMARY KEY (id);


--
-- TOC entry 4782 (class 2606 OID 49385)
-- Name: premio premio_pkey; Type: CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.premio
    ADD CONSTRAINT premio_pkey PRIMARY KEY (id);


--
-- TOC entry 4768 (class 2606 OID 49349)
-- Name: rifa rifa_codigo_key; Type: CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.rifa
    ADD CONSTRAINT rifa_codigo_key UNIQUE (codigo);


--
-- TOC entry 4770 (class 2606 OID 49347)
-- Name: rifa rifa_pkey; Type: CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.rifa
    ADD CONSTRAINT rifa_pkey PRIMARY KEY (id);


--
-- TOC entry 4790 (class 2606 OID 49434)
-- Name: sorteo_evento sorteo_evento_pkey; Type: CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.sorteo_evento
    ADD CONSTRAINT sorteo_evento_pkey PRIMARY KEY (id);


--
-- TOC entry 4796 (class 2606 OID 57367)
-- Name: sorteo_premio_config sorteo_premio_config_pkey; Type: CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.sorteo_premio_config
    ADD CONSTRAINT sorteo_premio_config_pkey PRIMARY KEY (id);


--
-- TOC entry 4794 (class 2606 OID 49516)
-- Name: sorteo_sesion sorteo_sesion_pkey; Type: CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.sorteo_sesion
    ADD CONSTRAINT sorteo_sesion_pkey PRIMARY KEY (id);


--
-- TOC entry 4792 (class 2606 OID 49436)
-- Name: sorteo_evento uq_evento_sesion_premio_indice; Type: CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.sorteo_evento
    ADD CONSTRAINT uq_evento_sesion_premio_indice UNIQUE (sorteo_sesion_id, premio_id, indice_salida);


--
-- TOC entry 4776 (class 2606 OID 49367)
-- Name: participante uq_participante_rifa_telefono; Type: CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.participante
    ADD CONSTRAINT uq_participante_rifa_telefono UNIQUE (rifa_id, telefono);


--
-- TOC entry 4784 (class 2606 OID 49387)
-- Name: premio uq_premio_rifa_nombre; Type: CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.premio
    ADD CONSTRAINT uq_premio_rifa_nombre UNIQUE (rifa_id, nombre);


--
-- TOC entry 4798 (class 2606 OID 57369)
-- Name: sorteo_premio_config uq_sorteo_premio_config; Type: CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.sorteo_premio_config
    ADD CONSTRAINT uq_sorteo_premio_config UNIQUE (sorteo_sesion_id, premio_id);


--
-- TOC entry 4763 (class 1259 OID 49336)
-- Name: idx_admin_user_activo; Type: INDEX; Schema: rifago; Owner: postgres
--

CREATE INDEX idx_admin_user_activo ON rifago.admin_user USING btree (activo);


--
-- TOC entry 4785 (class 1259 OID 49455)
-- Name: idx_evento_ganador; Type: INDEX; Schema: rifago; Owner: postgres
--

CREATE INDEX idx_evento_ganador ON rifago.sorteo_evento USING btree (es_ganador);


--
-- TOC entry 4786 (class 1259 OID 49454)
-- Name: idx_evento_participante; Type: INDEX; Schema: rifago; Owner: postgres
--

CREATE INDEX idx_evento_participante ON rifago.sorteo_evento USING btree (participante_id);


--
-- TOC entry 4787 (class 1259 OID 49453)
-- Name: idx_evento_premio; Type: INDEX; Schema: rifago; Owner: postgres
--

CREATE INDEX idx_evento_premio ON rifago.sorteo_evento USING btree (premio_id);


--
-- TOC entry 4788 (class 1259 OID 49452)
-- Name: idx_evento_sesion; Type: INDEX; Schema: rifago; Owner: postgres
--

CREATE INDEX idx_evento_sesion ON rifago.sorteo_evento USING btree (sorteo_sesion_id);


--
-- TOC entry 4771 (class 1259 OID 49373)
-- Name: idx_participante_rifa_id; Type: INDEX; Schema: rifago; Owner: postgres
--

CREATE INDEX idx_participante_rifa_id ON rifago.participante USING btree (rifa_id);


--
-- TOC entry 4772 (class 1259 OID 49374)
-- Name: idx_participante_telefono; Type: INDEX; Schema: rifago; Owner: postgres
--

CREATE INDEX idx_participante_telefono ON rifago.participante USING btree (telefono);


--
-- TOC entry 4777 (class 1259 OID 49396)
-- Name: idx_premio_ganador; Type: INDEX; Schema: rifago; Owner: postgres
--

CREATE INDEX idx_premio_ganador ON rifago.premio USING btree (ganador_participante_id);


--
-- TOC entry 4778 (class 1259 OID 49395)
-- Name: idx_premio_orden; Type: INDEX; Schema: rifago; Owner: postgres
--

CREATE INDEX idx_premio_orden ON rifago.premio USING btree (orden);


--
-- TOC entry 4779 (class 1259 OID 49394)
-- Name: idx_premio_precio; Type: INDEX; Schema: rifago; Owner: postgres
--

CREATE INDEX idx_premio_precio ON rifago.premio USING btree (precio);


--
-- TOC entry 4780 (class 1259 OID 49393)
-- Name: idx_premio_rifa_id; Type: INDEX; Schema: rifago; Owner: postgres
--

CREATE INDEX idx_premio_rifa_id ON rifago.premio USING btree (rifa_id);


--
-- TOC entry 4764 (class 1259 OID 49355)
-- Name: idx_rifa_admin_id; Type: INDEX; Schema: rifago; Owner: postgres
--

CREATE INDEX idx_rifa_admin_id ON rifago.rifa USING btree (admin_id);


--
-- TOC entry 4765 (class 1259 OID 49470)
-- Name: idx_rifa_estado; Type: INDEX; Schema: rifago; Owner: postgres
--

CREATE INDEX idx_rifa_estado ON rifago.rifa USING btree (estado);


--
-- TOC entry 4766 (class 1259 OID 49357)
-- Name: idx_rifa_fecha_rifa; Type: INDEX; Schema: rifago; Owner: postgres
--

CREATE INDEX idx_rifa_fecha_rifa ON rifago.rifa USING btree (fecha_rifa);


--
-- TOC entry 4809 (class 2620 OID 49467)
-- Name: sorteo_evento trg_validar_evento_consistencia_rifa; Type: TRIGGER; Schema: rifago; Owner: postgres
--

CREATE TRIGGER trg_validar_evento_consistencia_rifa BEFORE INSERT OR UPDATE ON rifago.sorteo_evento FOR EACH ROW EXECUTE FUNCTION rifago.fn_validar_evento_consistencia_rifa();


--
-- TOC entry 4801 (class 2606 OID 49397)
-- Name: premio fk_premio_ganador_participante; Type: FK CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.premio
    ADD CONSTRAINT fk_premio_ganador_participante FOREIGN KEY (ganador_participante_id) REFERENCES rifago.participante(id) ON DELETE SET NULL;


--
-- TOC entry 4807 (class 2606 OID 57375)
-- Name: sorteo_premio_config fk_spc_premio; Type: FK CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.sorteo_premio_config
    ADD CONSTRAINT fk_spc_premio FOREIGN KEY (premio_id) REFERENCES rifago.premio(id) ON DELETE CASCADE;


--
-- TOC entry 4808 (class 2606 OID 57370)
-- Name: sorteo_premio_config fk_spc_sorteo_sesion; Type: FK CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.sorteo_premio_config
    ADD CONSTRAINT fk_spc_sorteo_sesion FOREIGN KEY (sorteo_sesion_id) REFERENCES rifago.sorteo_sesion(id) ON DELETE CASCADE;


--
-- TOC entry 4800 (class 2606 OID 49368)
-- Name: participante participante_rifa_id_fkey; Type: FK CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.participante
    ADD CONSTRAINT participante_rifa_id_fkey FOREIGN KEY (rifa_id) REFERENCES rifago.rifa(id) ON DELETE CASCADE;


--
-- TOC entry 4802 (class 2606 OID 49388)
-- Name: premio premio_rifa_id_fkey; Type: FK CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.premio
    ADD CONSTRAINT premio_rifa_id_fkey FOREIGN KEY (rifa_id) REFERENCES rifago.rifa(id) ON DELETE CASCADE;


--
-- TOC entry 4799 (class 2606 OID 49350)
-- Name: rifa rifa_admin_id_fkey; Type: FK CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.rifa
    ADD CONSTRAINT rifa_admin_id_fkey FOREIGN KEY (admin_id) REFERENCES rifago.admin_user(id) ON DELETE RESTRICT;


--
-- TOC entry 4803 (class 2606 OID 49447)
-- Name: sorteo_evento sorteo_evento_participante_id_fkey; Type: FK CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.sorteo_evento
    ADD CONSTRAINT sorteo_evento_participante_id_fkey FOREIGN KEY (participante_id) REFERENCES rifago.participante(id) ON DELETE RESTRICT;


--
-- TOC entry 4804 (class 2606 OID 49442)
-- Name: sorteo_evento sorteo_evento_premio_id_fkey; Type: FK CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.sorteo_evento
    ADD CONSTRAINT sorteo_evento_premio_id_fkey FOREIGN KEY (premio_id) REFERENCES rifago.premio(id) ON DELETE RESTRICT;


--
-- TOC entry 4805 (class 2606 OID 49522)
-- Name: sorteo_sesion sorteo_sesion_created_by_admin_id_fkey; Type: FK CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.sorteo_sesion
    ADD CONSTRAINT sorteo_sesion_created_by_admin_id_fkey FOREIGN KEY (created_by_admin_id) REFERENCES rifago.admin_user(id);


--
-- TOC entry 4806 (class 2606 OID 49517)
-- Name: sorteo_sesion sorteo_sesion_rifa_id_fkey; Type: FK CONSTRAINT; Schema: rifago; Owner: postgres
--

ALTER TABLE ONLY rifago.sorteo_sesion
    ADD CONSTRAINT sorteo_sesion_rifa_id_fkey FOREIGN KEY (rifa_id) REFERENCES rifago.rifa(id);


-- Completed on 2025-12-18 13:59:07

--
-- PostgreSQL database dump complete
--

