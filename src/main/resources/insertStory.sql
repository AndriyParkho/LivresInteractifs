INSERT INTO Utilisateur (nom, prenom, email, password) VALUES
	('Parkhomenko', 'Andriy', 'andriy.parkhomenko@grenoble-inp.org', 'code');

INSERT INTO Histoire (titre, idauteur) VALUES
	('Random latin', 1);
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist) VALUES
	(1, 'latin first', 'Dein Syria per speciosam interpatet diffusa planitiem. hanc nobilitat Antiochia, 
						mundo cognita civitas, cui non certaverit alia advecticiis ita adfluere copiis et 
						internis, et Laodicia et Apamia itidemque Seleucia iam inde a primis auspiciis florentissimae.',
						2, 1, 1);
INSERT INTO Histoire (titre, idauteur) VALUES
	('Super histoire latine', 1);
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist) VALUES
	(1, 'latin encore', 'Eodem tempore Serenianus ex duce, cuius ignavia populatam in 
						Phoenice Celsen ante rettulimus, pulsatae maiestatis imperii reus 
						iure postulatus ac lege, incertum qua potuit suffragatione absolvi, 
						aperte convictus familiarem suum cum pileo, quo caput operiebat, incantato 
						vetitis artibus ad templum misisse fatidicum, quaeritatum expresse an ei firmum 
						portenderetur imperium, ut cupiebat, et cunctum..',
						3, 1, 2);
INSERT INTO Histoire (titre, idauteur) VALUES
	('Très grand latin', 1);
INSERT INTO Paragraphe(numparag, titre, texte, nbchoix, idwritter, idhist) VALUES
	(1, 'latin encore encore', 'Quam ob rem vita quidem talis fuit vel fortuna vel gloria, 
								ut nihil posset accedere, moriendi autem sensum celeritas abstulit; 
								quo de genere mortis difficile dictu est; quid homines suspicentur, videtis; 
								hoc vere tamen licet dicere, P. Scipioni ex multis diebus, quos in vita celeberrimos 
								laetissimosque viderit, illum diem clarissimum fuisse, cum senatu dimisso domum reductus 
								ad vesperum est a patribus conscriptis, populo Romano, sociis et Latinis, pridie quam excessit 
								e vita, ut ex tam alto dignitatis gradu ad superos videatur deos potius quam ad inferos pervenisse.',
								1, 1, 3);