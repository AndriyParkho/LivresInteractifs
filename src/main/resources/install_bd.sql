
DROP SEQUENCE idUtil_seq;
DROP SEQUENCE idHist_seq;

DROP TABLE HasRead;
DROP TABLE IsInvited;
DROP TABLE isFollowing;
DROP TABLE Paragraphe;
DROP TABLE Histoire;
DROP TABLE Utilisateur;


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

INSERT INTO Utilisateur (nom, prenom, email, password) VALUES
	('Parkhomenko', 'Andriy', 'andriy.parkhomenko@grenoble-inp.org', 'code');

INSERT INTO Utilisateur (nom, prenom, email, password) VALUES
	('J??z??quel-Royer', 'Louis', 'louis.jezequel-royer@grenoble-inp.org', 'code');
	
INSERT INTO Utilisateur (nom, prenom, email, password) VALUES
	('Ousset', 'Micka??l', 'mickael.ousset@grenoble-inp.org', 'code');

	
INSERT INTO Histoire (titre, idauteur, prive) VALUES
	('Les trois pi??ces d''or', 1, 1);
INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(1, 1, 'd??but', 'Tout commence ?? l''??poque o?? les humains vivaient en harmonie avec de nombreuses autres cr??atures comme les dragons, les elfes, les nains... La magie ??tait alors utilis??e par quelques initi??s... Durant ces temps oubli??s, de nobles guerriers parcouraient le monde ?? la recherche de richesses et luttaient vaillamment contre des cr??atures mal??fiques. Peut-??tre es-tu l''un de ces h??ros...  Ton aventure d??bute un matin ensoleill??, pr??s de la tani??re de ton ami Dragono. Il t''explique qu''il s''est absent?? toute la journ??e d''hier et en rentrant dans sa grotte, il a d??couvert qu''il lui manquait 3 pi??ces d''or ! Il aimerait savoir qui a bien pu lui voler les pr??cieuses pi??ces de son tr??sor ? Comme tu es son ami, il te demande de l''aider ?? retrouver ses pi??ces car il sait que tu es tr??s fort pour ce genre d''enqu??te.
Voici ce que contient ta sacoche:
-Un briquet ?? silex
-Une torche
-Une bourse avec 12 pi??ces de cuivre.',
						1, 1, 1);
INSERT INTO IsInvited(idhist, idutil) VALUES
	(1, 1);
INSERT INTO IsInvited(idhist, idutil) VALUES
	(1, 2);
INSERT INTO IsInvited(idhist, idutil) VALUES
	(1, 3);

INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(2, 1, 'Vas devant l''entr??e de la grotte pour commencer ton enqu??te', 'Tu es devant l''entr??e de la grotte de Dragono. Ton ami dragon esp??re que tu y trouveras des indices laiss??s par le voleur.',
						1, 2, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 1, 2);

INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(3, 1, 'Entre dans la grotte pour chercher des indices.', 'Tu allumes ta torche avec ton briquet ?? silex et commences ?? avancer dans la grotte. Les parois sont d''un noir luisant avec des reflets rougeoyants. Plus tu avances, plus le sol et le plafond se couvrent de stalagmites et de stalactites qui rendent la progression difficile (pour qui ne sait pas voler!) et donnent l''impression de marcher dans la gueule d''une immense b??te f??roce. Sur la pointe d''un stalagmite, tu trouves un lambeau de tissus marron.  Tu le prends et le mets dans ta sacoche.',
						1, 3, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 2, 3);
	
INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(4, 1, 'Continue d''avancer dans la grotte', 'Tu continues ?? marcher et les parois se mettent soudain ?? scintiller avec la lumi??re de ta torche. En approchant, tu d??couvres qu''elles rec??lent de pierres pr??cieuses. T''arr??tant un instant pour admirer ce spectacle, tu vois une fiole en verre soigneusement pos??e sur un rocher ?? c??t?? de toi. La fiole est ?? moiti?? remplie d''un liquide orange??tre. Tu ramasses avec pr??caution le fragile flacon.',
						1, 2, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 3, 4);

INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(5, 1, 'Continue d''avancer dans la grotte', 'En reprenant ton chemin, tu constates que les parois de la grotte s''??largissent. Puis, tu vois maintenant le sol se mettre ?? briller et semble remuer ?? la fa??on des reflets de la lune sur les flots d''une rivi??re lors d''une nuit claire. En t''abaissant, tu t''aper??ois que le sol est recouvert de milliers de pi??ces d''or qui illuminent la grotte ?? la lueur de ta torche ! Tu es arriv?? dans la plus vaste partie de la grotte, c''est l''antre du dragon. Dragono est d??j?? en train de t''attendre au milieu de sa tani??re.',
						1, 1, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 4, 5);

INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(6, 1, 'Discute avec Dragono', 'Dragono t''explique alors, qu''il a besoin de ce tr??sor pour vivre et se r??g??n??rer. C''est ainsi que survivent les dragons, sans boire ni manger mais pouvant vivre durant des si??cles voire des mill??naires ?? condition de dormir chaque nuit pr??s de leur tr??sor. Dragono peut se passer des trois pi??ces d''or qui ont disparues, mais il souhaite savoir qui ?? fait cela et pourquoi. En fouillant au centre de la grotte dans un creux form?? par les pi??ces, tu d??couvres une plume, dans ce qui semble ??tre le nid du dragon. Le dragon n''avait pas connaissance de ces 3 objets et te les laisse volontiers pour mener ton enqu??te.',
						1, 3, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 5, 6);

INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(7, 1, 'Ressort de la grotte', 'Te revoil?? devant l''entr??e de la grotte avec Dragono. Il remarque que tu n''as pas d''??quipement pour mener ?? bien cette aventure et te conseille de rem??dier ?? cela. Que veux-tu faire ?',
						3, 3, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 6, 7);

INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 7, 3);
	
INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(8, 1, 'A l''entr??e de la grotte, tu remarques des empreintes de pas', ' Te voil?? sur le sentier qui m??ne ?? la for??t. Le d??but du parcours est plut??t plat et te permet d''aller bon train et de profiter du paysage par cette belle journ??e ensoleill??e. Mais bient??t le chemin r??tr??cie et des passages plus escarp??s t''obligent ?? ralentir ton allure. Tu dois maintenant escalader des rochers enfonc??s dans un maquis fait de petits arbustes et buissons ??pineux qui ne permettent pas de s''??carter de cette ??troite piste. Puis, le maquis laisse la place ?? un paysage d''arbres plus hauts laissant entendre ??a et l?? les bruits de la for??t. 
Un peu plus loin, le sentier s''??largit ?? nouveau jusqu''?? arriver en face d''un ??norme rocher qui a la forme d''un visage de g??ant qui penche sur sa droite comme pour regarder plus bas. A cet endroit, la piste se s??pare en deux, l''une ?? droite et l''autre ?? gauche...La piste de gauche descend par un raidillon qui semble mener au plus profond de la for??t o?? la v??g??tation est plus dense et o?? les rayons du soleil peinent ?? se frayer un passage. La piste de droite remonte en serpentant sur les flans d''une colline pour rejoindre une clairi??re verdoyante.  De quel c??t?? vas-tu?',
						3, 1, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 7, 8);

INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(9, 1, 'Tu d??cides de te rendre au village', '  Te voil?? arriv?? dans l''auberge du nain !  C''est un endroit sombre, bruyant et malodorant, mais il est fr??quent?? par toutes sortes de personnages plus ou moins honn??tes qui pourront s??rement te renseigner.

En entrant dans l''auberge, tu parcours rapidement du regard l''ensemble de la salle. Il n''y a qu''une salle de forme carr??e avec des murs faits de bois et pierres sombres rappelant la roche des mines de nains situ??es dans les montagnes voisines. Sur le mur situ?? ?? droite, se trouve une fen??tre, trop petite pour laisser entrer la moindre lumi??re et qui semble avoir ??t?? construite par erreur.  Au centre de la pi??ce, une dizaine de tables avec des hommes et des femmes en tout genre mangeant, buvant, riant et criant pour se faire entendre. Au fond de l''auberge se trouve une grande chemin??e avec une marmite et une grosse pi??ce de viande sur une r??tissoire. Devant la chemin??e, un vieil elfe s''aff??re entre la marmite et une table recouverte de l??gumes et autres ingr??dients plus ou moins app??tissants. Sur la gauche, un long bar en bois d''??b??ne fonc?? et sculpt?? de toutes parts avec ?? chaque extr??mit?? une t??te de nain. L''une des t??tes grimace et l''autre est en col??re. Situ?? derri??re le comptoir se trouve un nain peu souriant avec une ??norme barbe. Tu souris en voyant que le nain balaye le zinc avec sa barbe ?? chaque fois qu''il se penche pour y poser ou prendre une chope de bi??re. C''est le ma??tre Aubergiste.

La plupart de ces personnes sont s??rement de simples villageois venus se restaurer, mais plusieurs individus retiennent ton attention... ',
						5, 2, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 7, 9);


INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 9, 7);
INSERT INTO Paragraphe(numparag, idhist, titre, texte, idwritter, valide, idModifier) VALUES
	(10, 1, 'Les 3 vilains', ' Tu t''approches vers les trois voyageurs en leur demandant si tu peux te joindre ?? eux. Les vilains cessent de ricaner et te d??visagent. Le plus petit des deux hommes, pousse son assiette devant lui et te r??pond: ??Ce serait avec plaisir mais moi et mes camarades devons reprendre la route??. Les trois vilains se l??vent en silence et ramassent leurs affaires. Le grand ahuri se baisse alors pour prendre le gros sac et le met sur son ??paule. Lorsqu''il passe pr??s de toi, tu remarques ?? nouveau le trou dans le sac qui correspond parfaitement au lambeau trouv?? dans la grotte. Les trois individus sortent ensuite l''auberge.
Que fais-tu? ',
						1, 0, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 9, 10);

INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide, idModifier) VALUES
	(11, 1, 'L''aubergiste', 'Tu t''approches vers les trois voyageurs en leur demandant si tu peux te joindre ?? eux. Les vilains cessent de ricaner et te d??visagent. Le plus petit des deux hommes, pousse son assiette devant lui et te r??pond: ??Ce serait avec plaisir mais moi et mes camarades devons reprendre la route??. Les trois vilains se l??vent en silence et ramassent leurs affaires. Le grand ahuri se baisse alors pour prendre le gros sac et le met sur son ??paule. Lorsqu''il passe pr??s de toi, tu remarques ?? nouveau le trou dans le sac qui correspond parfaitement au lambeau trouv?? dans la grotte. Les trois individus sortent ensuite l''auberge.
Que fais-tu? ',
						4 , 2, 0, 2);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 9, 11);

INSERT INTO Paragraphe(numparag, idhist, titre, idwritter, valide, idModifier) VALUES
	(12, 1, 'L''homme press??', 3, 0, 3);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 9, 12);
	
INSERT INTO Paragraphe(numparag, idhist, titre) VALUES
	(13, 1, 'L''elfe cuisinier');
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 9, 13);
	

INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(14, 1, 'Prends le raidillon qui descend sur ta gauche', 'Tu t''engages dans ce petit raidillon qui s''enfonce dans la partie la plus basse de la for??t. Au fur et ?? mesure que tu descends, le joyeux chant des oiseaux laisse la place ?? un silence pesant parfois rompu par le cri strident d''un corbeau. Plus tu avances, plus la v??g??tation verdoyante dispara??t se laissant doucement remplacer par des branches et des arbres morts. Seuls les plus grands arbres persistent et  levant les yeux vers la canop??e tu comprends qu''aucun rayons de soleil ne peut traverser la cime de ces arbres qui semblent n''avoir de feuilles que pour emp??cher la lumi??re de passer.  En continuant ta descente dans ce calme angoissant, tu te surprends parfois ?? regarder de part et d''autre pour t''assurer de ne pas ??tre observ??. Lorsque la descente se termine tu d??couvres un paysage mar??cageux dans lequel le sentier se jette et dispara??t dans des flaques de boue et d''eau stagnante. Devant toi, il n''y a plus aucun chemin.
Que fais-tu? ',
						2, 1, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 8, 14);
	
INSERT INTO Paragraphe(numparag, idhist, titre) VALUES
	(15, 1, 'Prends la piste de droite qui remonte vers la clairi??re');
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 8, 15);

INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 8, 9);
	

INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(16, 1, 'Tu continues dans la m??me direction et tu avances dans les mar??cages', 'P??n??trant dans les mar??cages, tu poursuis ta marche dans la m??me direction. Devant toi, il n''y a plus de sentier seulement un sol boueux parsem?? de flaques et bord?? par des arbres morts, des souches, des troncs et des branchages qui ne laissent pas d''autres choix que de suivre cette piste plus d??gag??e. Contournant et enjambant les ??tendues d''eau stagnante, tu entres dans une l??g??re brume. Une odeur de putr??faction se fait maintenant sentir. Puis, la brume devient brouillard et bient??t tu n''y vois plus ?? trois pas. Tu essaie d''avancer ?? t??tons mais tu t''enlises dans la vase jusqu''aux cuisses... Tu parviens ?? te d??gager de la tourbe lorsque la brume se dissipe enfin. Tu regardes autour de toi et tu ne reconnais plus du tout l''endroit d''o?? tu viens. Tu es perdu au milieu des mar??cages. D??sesp??r??, tu t''assois sur un tronc d''arbre en r??fl??chissant. Finalement, ton vieux chien Rodolphe vient te chercher pour rentrer ?? la maison.',
						0, 3, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 14, 16);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 14, 15);
	

INSERT INTO Histoire(titre, prive, datePubli, idAuteur) VALUES('Monsieur Charles', 0, SYSDATE, 2);

INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES (1, 'Le commencement', 'Aujourd''hui, comme tous les lundis, d??s la sortie de la classe, je cours vers le parc avec mes copains pour y retrouver monsieur Charles. J''essaye d''arriver le premier pour pouvoir choisir l''histoire qu''il va nous conter. Moi, je choisis toujours des histoires de d??tectives... Vite je dois me d??p??cher...', 1, 1, 2, 2);

INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES(2, 'Le parc', 'Nous voil?? arriv??s au parc. On rep??re toujours monsieur Charles de loin gr??ce ?? son grand panier rouge. Mais ce soir, le banc vert sur lequel monsieur Charles s???assoit est vide. Nous partons ?? sa recherche dans le parc...', 1, 3, 2, 2);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 1, 2);
INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES(3, 'Nous partons vers le grand arbre','Nous avons beau chercher, mais nous ne le trouvons toujours pas. Nous d??cidons de retourner vers le banc vert. ' ,1, 1, 2, 2);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 2, 3);
INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES(4, 'Nous allons vers le petit ruisseau', 'Il n???y a personne du c??t?? du petit ruisseau. Nous d??cidons de retourner vers le banc vert. Pour aller plus vite nous d??cidons de revenir par le petit talus.> Passez par le petit talus.', 1, 1, 2, 2);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 2, 4);
INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES(5, 'S''asseoir sur le banc', 'Nous nous asseyons sur le banc et attendons un peu. Mais personne ne vient. Pour la premi??re fois, nous n???aurons pas d???histoire...', 1, 2, 2, 2);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 3, 5);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 4, 5);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 5, 2);
INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES(6, 'Nous partons vers l''entr??e du parc', 'Arriv??s ?? l???entr??e du parc, nous voyons un petit monsieur avec un manteau gris. C???est lui!!! Nous l???appelons ?? Monsieur Charles!!!?? Mais il ne se retourne pas: ce n?????tait pas lui... nous d??cidons de retourner vers le banc vert. ', 1, 1, 2, 2);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils, conditionParag) VALUES(2, 2, 6, 4);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 6, 5);
INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES(7, 'Rentrer ?? la maison', 'Nous nous dirigeons vers la sortie du parc. Tout ?? coup Fabien crie: ?? Regardez, regardez, c???est le panier de monsieur Charles! ??. Il montrait une dame qui portait le panier rouge de notre ami. Tout ceci est bien myst??rieux.', 1, 2, 2, 2);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 5, 7);
INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES(8, 'Jouer ?? la GameBoy ?? la maison', 'Nous rentrons tous ?? la maison, on reviendra lundi prochain pour voir s???il sera l??... ', 1, 1, 2, 2);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 7, 8);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 8, 1);
INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES(9, 'Suivre la dame', 'Nous suivons la dame. Lucie avait un peu peur, mais je l???ai encourag??e: nous devons savoir ce qui est arriv?? ?? monsieur Charles. Nous courons jusqu????? la grille. A suivre...', 1, 0, 2, 2);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 7, 9);


INSERT INTO Histoire (titre, idauteur, prive) VALUES
	('Histoire ?? publier', 3, 0);
INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(1, 3, 'Un unique paragraphe', 'Ceci est une histoire ?? un paragraphe qui permet de tester la publication d''une histoire',
						0, 2, 1);


INSERT INTO Histoire (titre, idauteur, datePubli) VALUES
	('Random latin', 1, TO_DATE('03/04/2020', 'MM/DD/YYYY'));
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist, valide) VALUES
	(1, 'latin first', 'Dein Syria per speciosam interpatet diffusa planitiem. hanc nobilitat Antiochia, 
						mundo cognita civitas, cui non certaverit alia advecticiis ita adfluere copiis et 
						internis, et Laodicia et Apamia itidemque Seleucia iam inde a primis auspiciis florentissimae.',
						2, 1, 4, 1);
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist, valide) VALUES
	(2, 'latin second', 'Nec piget dicere avide magis hanc insulam populum Romanum invasisse quam iuste. 
						Ptolomaeo enim rege foederato nobis et socio ob aerarii nostri angustias iusso sine 
						ulla culpa proscribi ideoque hausto veneno voluntaria morte deleto et tributaria facta 
						est et velut hostiles eius exuviae classi inpositae in urbem advectae sunt per Catonem, 
						nunc repetetur ordo gestorum.',
						2, 1, 4, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(4, 1, 2);
INSERT INTO Paragraphe(numparag, titre, idhist) VALUES
	(3, 'latin trois', 4);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(4, 1, 3);
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist, valide) VALUES
	(4, 'latin quatre', 'Procedente igitur mox tempore cum adventicium nihil inveniretur, relicta ora maritima 
						in Lycaoniam adnexam Isauriae se contulerunt ibique densis intersaepientes itinera praetenturis 
						provincialium et viatorum opibus pascebantur.',
						2, 1, 4, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(4, 2, 4);
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist, valide) VALUES
	(5, 'latin cinq', 'Quo cognito Constantius ultra mortalem modum exarsit ac nequo casu idem Gallus 
						de futuris incertus agitare quaedam conducentia saluti suae per itinera conaretur, remoti 
						sunt omnes de industria milites agentes in civitatibus perviis.',
						1, 1, 4, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(4, 2, 5);
INSERT INTO Paragraphe(numparag, titre, idhist) VALUES
	(6, 'latin six', 4);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(4, 5, 6);
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist, valide) VALUES
	(7, 'latin sept', 'Saraceni tamen nec amici nobis umquam nec hostes optandi, ultro citroque discursantes 
						quicquid inveniri poterat momento temporis parvi vastabant milvorum rapacium similes, qui si 
						praedam dispexerint celsius, volatu rapiunt celeri, aut nisi impetraverint, non inmorantur.',
						0, 1, 4, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(4, 4, 7);
INSERT INTO Paragraphe(numparag, titre, idhist) VALUES
	(8, 'latin huit', 4);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(4, 4, 8);
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist, valide) VALUES
	(9, 'latin neuf', 'Ego vero sic intellego, Patres conscripti, nos hoc tempore in provinciis decernendis perpetuae pacis habere oportere rationem. Nam quis hoc non sentit omnia alia esse nobis vacua ab omni periculo atque etiam suspicione belli?',
						1, 1, 4, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(4, 2, 9);
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist, valide) VALUES
	(10, 'latin dix', 'Hinc ille commotus ut iniusta perferens et indigna praefecti custodiam protectoribus mandaverat fidis. quo conperto Montius tunc quaestor acer quidem sed ad lenitatem propensior, consulens in commune advocatos palatinarum primos scholarum adlocutus est mollius docens nec decere haec fieri nec prodesse addensque vocis obiurgatorio sonu quod si id placeret, post statuas Constantii deiectas super adimenda vita praefecto conveniet securius cogitari.',
						0, 1, 4, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(4, 9, 10);

INSERT INTO Histoire (titre, idauteur, datePubli) VALUES
	('Une histoire compl??te ?? lire', 1, SYSDATE);

INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (1, 'Premier paragraphe ?? lire', 'Corps du premier paragraphe, d??j?? la fin',  0, 1, 5, 1);


INSERT INTO Histoire (titre, idauteur, datePubli) VALUES
	('Une histoire ?? publier', 1, NULL);

INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (1, 'Premier paragraphe ?? publier', 'Corps du premier paragraphe, comment ??a d??j?? la fin',  0, 1, 6, 1);

INSERT INTO Histoire (titre, idauteur, datePubli) VALUES
	('Une histoire avec un paragraphe ?? valider', 1, NULL);

INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (1, 'Premier paragraphe ?? valider', 'Corps du premier paragraphe, d??j?? la fin',  0, 1, 7, 1);


INSERT INTO Histoire (titre, idauteur, datePubli) VALUES
	('Une histoire avec un paragraphe ?? valider', 1, NULL);

INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (1, 'Premier paragraphe ?? valider', 'Corps du premier paragraphe, validez moi',  0, 1, 8, 1);


INSERT INTO Histoire (titre, idauteur, datePubli) VALUES
	('Une histoire ?? plusieurs chemins pour l''historique', 1, SYSDATE);

INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (1, 'Premier paragraphe ?? valider', 'Corps du premier paragraphe, d??j?? la fin',  2, 1, 9, 1);

INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (2, 'Second paragraphe, premier choix', 'Second paragraphe, premier chhoix',  1, 1, 9, 1);

INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (3, 'Second paragraphe, deuxi??me choix', 'Second paragraphe, deuxi??me chhoix',  1, 1, 9, 1);

INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (4, 'Dernier paragraphe, premier choix', 'Essayez de changer votre choix ?? l''aide de l''historique',  0, 1, 9, 1);

INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(9, 1, 2);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(9, 1, 3);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(9, 2, 4);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(9, 3, 4);


INSERT INTO Histoire ( titre, idauteur, datePubli, prive) VALUES
	('Une histoire sur laquelle je suis invit??', 2, NULL, 1);

INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (1, 'Premier paragrapahe, invitation', 'Je suis juste ici pour voir si les invitations fonctionnent',  0, NULL, 10, 0);
INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (2, 'Deuxieme paragraphe, invitation', 'Je suis juste ici pour voir si les invitations fonctionnent, je suis le second paragrpahe',  0, NULL, 10, 0);

INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(10, 1, 2);
INSERT INTO isInvited(idHist, idUtil) VALUES(10, 1);


INSERT INTO Histoire ( titre, idauteur, datePubli, prive) VALUES
	('Une histoire ?? supprimer', 1, NULL, 0);
INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (1, 'Premier paragraphe, a supprimer', 'Je vais ??tre supprim??',  2, 1, 11, 1);

INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (2, 'Deuxieme paragraphe, choix 1, a supprimer', 'Je vais ??tre supprim??',  0, NULL, 11, 0);
INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (3, 'Deuxieme paragraphe choix 2, a supprimer', 'Je vais ??tre supprim??',  0, NULL, 11, 0);


INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(11, 1, 2);

INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(11, 1, 3);
	
INSERT INTO Histoire ( titre, idauteur, datePubli, prive) VALUES
	('Une histoire ?? condition', 3, SYSDATE, 0);
	
INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (1, 'Premier paragraphe','En fonction de votre prochain choix, le quatri??me paragraphe propos?? ne sera pas le m??me.', 2, 3, 12, 1);
        
INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (2, 'Deuxi??me paragraphe','Vous avez choisi le deuxi??me paragraphe.', 1, 3, 12, 1);
        
INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (3, 'Troisi??me paragraphe','Vous avez choisi le troisi??me paragraphe.', 1, 3, 12, 1);
        
INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (4, 'Quatri??me paragraphe','Comme vous le voyez, vous avez un choix conditionnel.', 3, 3, 12, 1);
        
INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (5, 'Choix conditionnel, vous ??tes pass?? par le deuxi??me paragraphe','Comme vous le voyez, vous avez un choix conditionnel.', 0, 3, 12, 1);
        
INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (6, 'Choix conditionnel, vous ??tes pass?? par le troisi??me paragraphe','Comme vous le voyez, vous avez un choix conditionnel.', 0, 3, 12, 1);
        
INSERT INTO Paragraphe(numParag, titre, texte, nbChoix, idWritter, idHist, valide) VALUES
        (7, 'Choix inconditionnel','Comme vous le voyez, vous avez un choix inconditionnel.', 0, 3, 12, 1);
        
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(12, 1, 2);
	
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(12, 1, 3);
	
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(12, 2, 4);
	
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(12, 3, 4);
	
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(12, 4, 7);
	
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils, conditionParag) VALUES
	(12, 4, 5, 2);
	
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils, conditionParag) VALUES
	(12, 4, 6, 3);
