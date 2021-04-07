INSERT INTO Utilisateur (nom, prenom, email, password) VALUES
	('Parkhomenko', 'Andriy', 'andriy.parkhomenko@grenoble-inp.org', 'code');

INSERT INTO Histoire (titre, idauteur, datePubli) VALUES
	('Random latin', 1, TO_DATE('03/04/2020', 'MM/DD/YYYY'));
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist, valide) VALUES
	(1, 'latin first', 'Dein Syria per speciosam interpatet diffusa planitiem. hanc nobilitat Antiochia, 
						mundo cognita civitas, cui non certaverit alia advecticiis ita adfluere copiis et 
						internis, et Laodicia et Apamia itidemque Seleucia iam inde a primis auspiciis florentissimae.',
						2, 1, 1, 1);
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist, valide) VALUES
	(2, 'latin second', 'Nec piget dicere avide magis hanc insulam populum Romanum invasisse quam iuste. 
						Ptolomaeo enim rege foederato nobis et socio ob aerarii nostri angustias iusso sine 
						ulla culpa proscribi ideoque hausto veneno voluntaria morte deleto et tributaria facta 
						est et velut hostiles eius exuviae classi inpositae in urbem advectae sunt per Catonem, 
						nunc repetetur ordo gestorum.',
						2, 1, 1, 1);
INSERT INTO IsFollowing(idhistpere, numparagpere, numchoix, idhistfils, numparagfils) VALUES
	(1, 1, 1, 1, 2);
INSERT INTO Paragraphe(numparag, titre, idhist) VALUES
	(3, 'latin trois', 1);
INSERT INTO IsFollowing(idhistpere, numparagpere, numchoix, idhistfils, numparagfils) VALUES
	(1, 1, 2, 1, 3);
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist, valide) VALUES
	(4, 'latin quatre', 'Procedente igitur mox tempore cum adventicium nihil inveniretur, relicta ora maritima 
						in Lycaoniam adnexam Isauriae se contulerunt ibique densis intersaepientes itinera praetenturis 
						provincialium et viatorum opibus pascebantur.',
						2, 1, 1, 1);
INSERT INTO IsFollowing(idhistpere, numparagpere, numchoix, idhistfils, numparagfils) VALUES
	(1, 2, 1, 1, 4);
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist, valide) VALUES
	(5, 'latin cinq', 'Quo cognito Constantius ultra mortalem modum exarsit ac nequo casu idem Gallus 
						de futuris incertus agitare quaedam conducentia saluti suae per itinera conaretur, remoti 
						sunt omnes de industria milites agentes in civitatibus perviis.',
						1, 1, 1, 1);
INSERT INTO IsFollowing(idhistpere, numparagpere, numchoix, idhistfils, numparagfils) VALUES
	(1, 2, 2, 1, 5);
INSERT INTO Paragraphe(numparag, titre, idhist) VALUES
	(6, 'latin six', 1);
INSERT INTO IsFollowing(idhistpere, numparagpere, numchoix, idhistfils, numparagfils) VALUES
	(1, 5, 1, 1, 6);
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist, valide) VALUES
	(7, 'latin sept', 'Saraceni tamen nec amici nobis umquam nec hostes optandi, ultro citroque discursantes 
						quicquid inveniri poterat momento temporis parvi vastabant milvorum rapacium similes, qui si 
						praedam dispexerint celsius, volatu rapiunt celeri, aut nisi impetraverint, non inmorantur.',
						0, 1, 1, 1);
INSERT INTO IsFollowing(idhistpere, numparagpere, numchoix, idhistfils, numparagfils) VALUES
	(1, 4, 1, 1, 7);
INSERT INTO Paragraphe(numparag, titre, idhist) VALUES
	(8, 'latin huit', 1);
INSERT INTO IsFollowing(idhistpere, numparagpere, numchoix, idhistfils, numparagfils) VALUES
	(1, 4, 1, 1, 8);
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist, valide) VALUES
	(9, 'latin neuf', 'Ego vero sic intellego, Patres conscripti, nos hoc tempore in provinciis decernendis perpetuae pacis habere oportere rationem. Nam quis hoc non sentit omnia alia esse nobis vacua ab omni periculo atque etiam suspicione belli?',
						1, 1, 1, 1);
INSERT INTO IsFollowing(idhistpere, numparagpere, numchoix, idhistfils, numparagfils) VALUES
	(1, 2, 1, 1, 9);
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist, valide) VALUES
	(10, 'latin dix', 'Hinc ille commotus ut iniusta perferens et indigna praefecti custodiam protectoribus mandaverat fidis. quo conperto Montius tunc quaestor acer quidem sed ad lenitatem propensior, consulens in commune advocatos palatinarum primos scholarum adlocutus est mollius docens nec decere haec fieri nec prodesse addensque vocis obiurgatorio sonu quod si id placeret, post statuas Constantii deiectas super adimenda vita praefecto conveniet securius cogitari.',
						0, 1, 1, 1);
INSERT INTO IsFollowing(idhistpere, numparagpere, numchoix, idhistfils, numparagfils) VALUES
	(1, 9, 1, 1, 10);
						
INSERT INTO Histoire (titre, idauteur, datePubli) VALUES
	('Super histoire latine', 1, TO_DATE('03/04/2020', 'MM/DD/YYYY'));
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist) VALUES
	(1, 'latin encore', 'Eodem tempore Serenianus ex duce, cuius ignavia populatam in 
						Phoenice Celsen ante rettulimus, pulsatae maiestatis imperii reus 
						iure postulatus ac lege, incertum qua potuit suffragatione absolvi, 
						aperte convictus familiarem suum cum pileo, quo caput operiebat, incantato 
						vetitis artibus ad templum misisse fatidicum, quaeritatum expresse an ei firmum 
						portenderetur imperium, ut cupiebat, et cunctum..',
						3, 1, 2);
INSERT INTO Histoire (titre, idauteur, datePubli) VALUES
	('Très grand latin', 1, TO_DATE('03/04/2020', 'MM/DD/YYYY'));
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist) VALUES
	(1, 'latin encore encore', 'Quam ob rem vita quidem talis fuit vel fortuna vel gloria, 
								ut nihil posset accedere, moriendi autem sensum celeritas abstulit; 
								quo de genere mortis difficile dictu est; quid homines suspicentur, videtis; 
								hoc vere tamen licet dicere, P. Scipioni ex multis diebus, quos in vita celeberrimos 
								laetissimosque viderit, illum diem clarissimum fuisse, cum senatu dimisso domum reductus 
								ad vesperum est a patribus conscriptis, populo Romano, sociis et Latinis, pridie quam excessit 
								e vita, ut ex tam alto dignitatis gradu ad superos videatur deos potius quam ad inferos pervenisse.',
								1, 1, 3);