
CREATE SEQUENCE idUtil_seq;
CREATE TABLE Utilisateur (
       idUtil INT DEFAULT idUtil_seq.nextval PRIMARY KEY,
       nom varchar(50) NOT NULL,
       prenom varchar(50) NOT NULL,
       email varchar(255) NOT NULL UNIQUE,
       password varchar(255) NOT NULL
);
CREATE SEQUENCE idHist_seq;
CREATE TABLE Histoire (
       idHist integer DEFAULT idHist_seq.nextval PRIMARY KEY,
       titre varchar(1000) NOT NULL,
       prive NUMBER(1,0) DEFAULT 0,
       datePubli date, -- Si Null donc histoire non publie
       idAuteur integer NOT NULL REFERENCES Utilisateur(idUtil)
);

CREATE TABLE Paragraphe (
       idHist integer NOT NULL REFERENCES Histoire (idHist) ON DELETE CASCADE,
       numParag integer NOT NULL,   
       titre varchar2(1000),
       texte varchar2(4000),
       valide NUMBER(1,0) DEFAULT 0,
       nbChoix integer DEFAULT 0,
       idWritter integer REFERENCES Utilisateur(idUtil),
       idModifier integer REFERENCES Utilisateur(idUtil),
       constraint pkParag PRIMARY KEY (numParag, idHist)
);

CREATE TABLE IsInvited (
       idHist integer NOT NULL REFERENCES Histoire(idHist) ON DELETE CASCADE,
       idUtil integer NOT NULL REFERENCES Utilisateur(idUtil),
       CONSTRAINT pkInvited PRIMARY KEY (idHist, idUtil)
);

CREATE TABLE HasRead (
       idHist integer NOT NULL,
       numParag integer NOT NULL,
       idUtil integer NOT NULL REFERENCES Utilisateur(idUtil),
       locationId integer NOT NULL,
       CHECK(locationId >= 0),
       CONSTRAINT fkHasRead FOREIGN KEY (idHist, numParag) REFERENCES Paragraphe(idHist, numParag) ON DELETE CASCADE,
       CONSTRAINT pkHasRead PRIMARY KEY (idHist, numParag, idUtil, locationId)
);

CREATE TABLE IsFollowing (
       idHistParag integer NOT NULL,
       numParagPere integer NOT NULL,
       numParagFils integer NOT NULL,
       conditionParag integer NULL,
       CONSTRAINT fkFollowingPere FOREIGN KEY (idHistParag, numParagPere) REFERENCES Paragraphe(idHist, numParag) ON DELETE CASCADE,
       CONSTRAINT fkFollowingFils FOREIGN KEY (idHistParag, numParagFils) REFERENCES Paragraphe(idHist, numParag) ON DELETE CASCADE,
       CONSTRAINT fkCondParag FOREIGN KEY(idHistParag, conditionParag) REFERENCES Paragraphe(idhist, numParag),
       CONSTRAINT pkIsFollowing PRIMARY KEY (idHistParag, numParagPere, numParagFils)
);

