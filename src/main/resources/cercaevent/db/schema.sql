-- Esquema H2 per a CercaEvent: estructura de taules i dades d'exemple
-- Projecte: CercaEvent
-- Motor de base de dades: H2 (configuració d'exemple)
-- Les contrasenyes d'exemple estan encriptades amb BCrypt
-- Les contrasenyes en text pla només apareixen als comentaris per facilitar proves locals

DROP TABLE IF EXISTS inscripcions;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS usuaris;

--------------------------------------------------
-- Taula: usuaris (estructura i restriccions)
--------------------------------------------------
CREATE TABLE usuaris (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuari VARCHAR(20) NOT NULL UNIQUE,
    nom VARCHAR(100) NOT NULL,
    cognoms VARCHAR(150),
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    data_registre TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rol VARCHAR(12) DEFAULT 'USER',
    CONSTRAINT chk_usuaris_rol CHECK (rol IN ('ADMIN', 'USER'))
);

--------------------------------------------------
-- Taula: events (camps comuns i camps específics per categoria)
--------------------------------------------------
CREATE TABLE events (
    id INT AUTO_INCREMENT PRIMARY KEY,

    -- Camps comuns de l'event (atributs generals)
    titol VARCHAR(200) NOT NULL,
    descripcio CLOB,
    ubicacio VARCHAR(150) NOT NULL,
    data_event DATE NOT NULL,
    hora_event TIME NOT NULL,
    aforament INT NOT NULL,
    places_disponibles INT NOT NULL,
    categoria VARCHAR(20) NOT NULL,
    creador_id INT NOT NULL,

    -- Camps específics per categoria 'Esport'
    tipus_esport VARCHAR(100),
    nivell VARCHAR(50),
    material_necessari VARCHAR(255),

    -- Camps específics per categoria 'Videojoc'
    joc VARCHAR(100),
    plataforma VARCHAR(100),
    modalitat VARCHAR(100),

    -- Camps específics per categoria 'Trobada'
    tema VARCHAR(100),
    tipus_trobada VARCHAR(100),
    edat_minima INT,

    CONSTRAINT fk_events_creador
        FOREIGN KEY (creador_id)
        REFERENCES usuaris(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_events_categoria
        CHECK (categoria IN ('Esport', 'Videojoc', 'Trobada')),

    CONSTRAINT chk_events_aforament
        CHECK (aforament >= 0),

    CONSTRAINT chk_events_places
        CHECK (places_disponibles >= 0 AND places_disponibles <= aforament),

    CONSTRAINT chk_events_edat_minima
        CHECK (edat_minima IS NULL OR edat_minima >= 0)
);

--------------------------------------------------
-- Taula: inscripcions (relació entre usuaris i events)
--------------------------------------------------
CREATE TABLE inscripcions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuari_id INT NOT NULL,
    event_id INT NOT NULL,
    data_inscripcio TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT fk_inscripcions_usuari
        FOREIGN KEY (usuari_id)
        REFERENCES usuaris(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_inscripcions_event
        FOREIGN KEY (event_id)
        REFERENCES events(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_inscripcio_unique
        UNIQUE (usuari_id, event_id)
);

--------------------------------------------------
-- Índexs recomanats per optimitzar consultes comunes
--------------------------------------------------
CREATE INDEX idx_usuaris_usuari ON usuaris(usuari);
CREATE INDEX idx_usuaris_email ON usuaris(email);
CREATE INDEX idx_events_categoria ON events(categoria);
CREATE INDEX idx_events_data_event ON events(data_event);
CREATE INDEX idx_events_creador_id ON events(creador_id);
CREATE INDEX idx_inscripcions_usuari_id ON inscripcions(usuari_id);
CREATE INDEX idx_inscripcions_event_id ON inscripcions(event_id);

--------------------------------------------------
-- Dades d'exemple: usuaris (comptes de prova)
--------------------------------------------------
-- Contrasenya en clar per proves: admin
INSERT INTO usuaris (usuari, nom, cognoms, email, password_hash, rol) VALUES
('admin', 'Administrador', 'Sistema', 'admin@cercaevent.cat', '$2a$12$rpK8orU8.bYa3nRhrgE40ePMXZq5wCPkSZ/FtwtRUVEAYjBX.6QGy', 'ADMIN');

-- Contrasenya en clar per proves: marta123
INSERT INTO usuaris (usuari, nom, cognoms, email, password_hash, rol) VALUES
('marta', 'Marta', 'Soler', 'marta@cercaevent.cat', '$2a$12$Tq.UlgYBpRImSVahIQCNVeUMLt9uuF36B7JPOvhm2iUfM5qd95HnK', 'USER');
-- Contrasenya en clar per proves: joan123
INSERT INTO usuaris (usuari, nom, cognoms, email, password_hash, rol) VALUES
('joan', 'Joan', 'Serra', 'joan@cercaevent.cat', '$2a$12$6cvOfQ97fOGWzj2cwknRCeb/dRnOLvrcgSPxDq7yHFdluGumiMIHe', 'USER');

-- Contrasenya en clar per proves: laura123
INSERT INTO usuaris (usuari, nom, cognoms, email, password_hash, rol) VALUES
('laura', 'Laura', 'Casas', 'laura@cercaevent.cat', '$2a$12$KiL3tzdb2j4b5ouq5/CMVONfSq7pShLCUob9dyfzZAm72AQQNKUlK', 'USER');
-- Contrasenya en clar per proves: pau123
INSERT INTO usuaris (usuari, nom, cognoms, email, password_hash, rol) VALUES
('pau', 'Pau', 'Ribas', 'pau@cercaevent.cat', '$2a$12$oRchPQTN9fjlYno2hezXhusG8AHoPXFpApSjEPZNcI3in3xIK7cOi', 'USER');

-- Contrasenya en clar per proves: anna123
INSERT INTO usuaris (usuari, nom, cognoms, email, password_hash, rol) VALUES
('anna', 'Anna', 'Vila', 'anna@cercaevent.cat', '$2a$12$4Abw3d3LaEeu1QhZ9Cd.qekzjuxWPvdxQUTzqbU6gIpgVDsGj2fXW', 'USER');
--------------------------------------------------
-- Dades d'exemple: events (distints tipus)
--------------------------------------------------

-- Categoria: ESPORT
INSERT INTO events (
    titol, descripcio, ubicacio, data_event, hora_event,
    aforament, places_disponibles, categoria, creador_id,
    tipus_esport, nivell, material_necessari
) VALUES
(
    'Partit de futbol 7',
    'Partit amistós entre jugadors amateurs.',
    'Girona',
    DATE '2026-05-10',
    TIME '18:30:00',
    14, 12, 'Esport', 2,
    'Futbol', 'Intermedi', 'Botes i canyelleres'
),
(
    'Sessió de running al parc',
    'Entrenament suau en grup per a tots els nivells.',
    'Barcelona',
    DATE '2026-05-12',
    TIME '07:30:00',
    20, 20, 'Esport', 3,
    'Running', 'Tots els nivells', 'Aigua i sabatilles'
),
(
    'Torneig de pàdel',
    'Torneig per parelles amb fase de grups.',
    'Tarragona',
    DATE '2026-05-18',
    TIME '10:00:00',
    16, 13, 'Esport', 4,
    'Pàdel', 'Avançat', 'Pala i roba esportiva'
);

-- Categoria: VIDEOJOC
INSERT INTO events (
    titol, descripcio, ubicacio, data_event, hora_event,
    aforament, places_disponibles, categoria, creador_id,
    joc, plataforma, modalitat
) VALUES
(
    'Torneig d''EA Sports FC',
    'Competició eliminatòria 1vs1 oberta a tothom.',
    'Lleida',
    DATE '2026-05-20',
    TIME '17:00:00',
    16, 13, 'Videojoc', 3,
    'EA Sports FC 26', 'PlayStation 5', '1vs1'
),
(
    'Nit de Mario Kart',
    'Partides amistoses i classificació final.',
    'Reus',
    DATE '2026-05-22',
    TIME '20:00:00',
    12, 10, 'Videojoc', 5,
    'Mario Kart 8 Deluxe', 'Nintendo Switch', 'Multijugador local'
),
(
    'Customs de League of Legends',
    'Partides 5vs5 entre equips creats a l''instant.',
    'Sabadell',
    DATE '2026-05-24',
    TIME '18:00:00',
    10, 9, 'Videojoc', 2,
    'League of Legends', 'PC', '5vs5'
);

-- Categoria: TROBADA
INSERT INTO events (
    titol, descripcio, ubicacio, data_event, hora_event,
    aforament, places_disponibles, categoria, creador_id,
    tema, tipus_trobada, edat_minima
) VALUES
(
    'Club de lectura',
    'Debat sobre literatura contemporània.',
    'Girona',
    DATE '2026-05-14',
    TIME '18:00:00',
    25, 22, 'Trobada', 4,
    'Literatura', 'Cultural', 16
),
(
    'Meetup de programació Java',
    'Sessió per compartir dubtes i projectes de JavaFX.',
    'Barcelona',
    DATE '2026-05-25',
    TIME '19:30:00',
    30, 26, 'Trobada', 1,
    'Programació', 'Tecnològica', 18
),
(
    'Intercanvi d''idiomes',
    'Trobada social per practicar anglès i francès.',
    'Manresa',
    DATE '2026-05-28',
    TIME '18:30:00',
    20, 20, 'Trobada', 6,
    'Idiomes', 'Social', 14
);

--------------------------------------------------
-- Dades d'exemple: inscripcions (mostra d'inscripcions i aforaments)
--------------------------------------------------

-- Exemple: event 1 té 2 inscrits (14 - 2 = 12)
INSERT INTO inscripcions (usuari_id, event_id, data_inscripcio) VALUES
(3, 1, TIMESTAMP '2026-04-20 10:00:00'),
(5, 1, TIMESTAMP '2026-04-20 10:30:00');

-- Exemple: event 3 té 3 inscrits (16 - 3 = 13)
INSERT INTO inscripcions (usuari_id, event_id, data_inscripcio) VALUES
(2, 3, TIMESTAMP '2026-04-21 09:15:00'),
(3, 3, TIMESTAMP '2026-04-21 09:45:00'),
(6, 3, TIMESTAMP '2026-04-21 10:10:00');

-- Exemple: event 4 té 3 inscrits (16 - 3 = 13)
INSERT INTO inscripcions (usuari_id, event_id, data_inscripcio) VALUES
(2, 4, TIMESTAMP '2026-04-22 16:00:00'),
(4, 4, TIMESTAMP '2026-04-22 16:05:00'),
(6, 4, TIMESTAMP '2026-04-22 16:10:00');

-- Exemple: event 5 té 2 inscrits (12 - 2 = 10)
INSERT INTO inscripcions (usuari_id, event_id, data_inscripcio) VALUES
(2, 5, TIMESTAMP '2026-04-23 18:00:00'),
(3, 5, TIMESTAMP '2026-04-23 18:20:00');

-- Exemple: event 6 té 1 inscrit (10 - 1 = 9)
INSERT INTO inscripcions (usuari_id, event_id, data_inscripcio) VALUES
(5, 6, TIMESTAMP '2026-04-24 17:40:00');

-- Exemple: event 7 té 3 inscrits (25 - 3 = 22)
INSERT INTO inscripcions (usuari_id, event_id, data_inscripcio) VALUES
(2, 7, TIMESTAMP '2026-04-25 09:00:00'),
(3, 7, TIMESTAMP '2026-04-25 09:05:00'),
(5, 7, TIMESTAMP '2026-04-25 09:10:00');

-- Exemple: event 8 té 4 inscrits (30 - 4 = 26)
INSERT INTO inscripcions (usuari_id, event_id, data_inscripcio) VALUES
(2, 8, TIMESTAMP '2026-04-26 19:00:00'),
(3, 8, TIMESTAMP '2026-04-26 19:02:00'),
(4, 8, TIMESTAMP '2026-04-26 19:04:00'),
(6, 8, TIMESTAMP '2026-04-26 19:06:00');