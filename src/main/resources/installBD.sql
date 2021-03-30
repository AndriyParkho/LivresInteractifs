CREATE SEQUENCE idUtil_seq;
CREATE TABLE Utilisateur (
       idUtil integer PRIMARY KEY,
       nom varchar(50) NOT NULL,
       prenom varchar(50) NOT NULL,
       email varchar(255) NOT NULL,
       password varchar(255) NOT NULL
);

CREATE SEQUENCE idHist_seq;
CREATE TABLE Histoire (
       idHist integer DEFAULT idHist_seq.nextval PRIMARY KEY,
       titre varchar(1000) NOT NULL,
       datePubli date, -- Si Null donc histoire non publie
       idAuteur integer NOT NULL REFERENCES Utilisateur(idUtil)
);

CREATE TABLE Paragraphe (
       numParag integer NOT NULL,
       idHist integer NOT NULL REFERENCES Histoire(idHist) ON DELETE CASCADE, --la supression d'une histoire supprime ses parag
       titre varchar(1000),
       texte varchar(max),
       valide boolean DEFAULT false,
       nbChoix interger NOT NULL,
       idWritter integer REFERENCES Utilisateur(idUtil),
       CONSTRAINT pkParag PRIMARY KEY (numParag, idHist)
);

CREATE TABLE IsInvited (
       idHist integer NOT NULL REFERENCES Histoire(idHist) ON DELETE CASCADE, --la supression d'une histoire supprime les invit
       idUtil integer NOT NULL REFERENCES Utilisateur(idUtil),
       CONSTRAINT pkInvited PRIMARY KEY (idHist, idUtil)
);

CREATE TABLE HasRead (
       idHist integer NOT NULL,
       numParag integer NOT NULL,
       idUtil integer NOT NULL REFERENCES Utilisateur(idUtil),
       CONSTRAINT fkHasRead FOREIGN KEY (idHist, numParag) REFERENCES Paragraphe(idHist, numParag) ON DELETE CASCADE,
       CONSTRAINT pkHasRead PRIMARY KEY (idHist, numParag, idUtil)
);

CREATE TABLE IsFollowing (
       idHistPere integer NOT NULL,
       numParagPere integer NOT NULL,
       numChoix integer NOT NULL,
       idHistFils integer NOT NULL,
       numParagFils integer NOT NULL,
       CONSTRAINT fkFollowingPere FOREIGN KEY (idHistPere, numParagPere) REFERENCES Paragraphe(idHist, numParag) ON DELETE CASCADE,
       CONSTRAINT fkFollowingFils FOREIGN KEY (idHistFils, numParagFils) REFERENCES Paragraphe(idHist, numParag) ON DELETE CASCADE,
       CONSTRAINT pkHasRead PRIMARY KEY (idHistPere, numParagPere, numChoix, idHistFils, numParagFils)
);

--INSERT INTO bibliographie (auteur, titre) VALUES
--   ('Jules Verne','Voyage au centre de la terre');
--INSERT INTO bibliographie (auteur, titre) VALUES
--   ('Arnaldur Indriðason','L’homme du lac');
--INSERT INTO bibliographie (auteur, titre) VALUES
--   ('Victor Hugo','Les misérables');