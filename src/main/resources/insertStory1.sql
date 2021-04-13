-- Première histoire
INSERT INTO Histoire (idhist, titre, idauteur, prive) VALUES
	(1, 'Les trois pièces d''or', 1, 1);
INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(1, 1, 'début', 'Tout commence à l''époque où les humains vivaient en harmonie avec de nombreuses autres créatures comme les dragons, les elfes, les nains... La magie était alors utilisée par quelques initiés... Durant ces temps oubliés, de nobles guerriers parcouraient le monde à la recherche de richesses et luttaient vaillamment contre des créatures maléfiques. Peut-être es-tu l''un de ces héros...  Ton aventure débute un matin ensoleillé, près de la tanière de ton ami Dragono. Il t''explique qu''il s''est absenté toute la journée d''hier et en rentrant dans sa grotte, il a découvert qu''il lui manquait 3 pièces d''or ! Il aimerait savoir qui a bien pu lui voler les précieuses pièces de son trésor ? Comme tu es son ami, il te demande de l''aider à retrouver ses pièces car il sait que tu es très fort pour ce genre d''enquête.
Voici ce que contient ta sacoche:
-Un briquet à silex
-Une torche
-Une bourse avec 12 pièces de cuivre.',
						1, 1, 1);
INSERT INTO IsInvited(idhist, idutil) VALUES
	(1, 1);
INSERT INTO IsInvited(idhist, idutil) VALUES
	(1, 2);
INSERT INTO IsInvited(idhist, idutil) VALUES
	(1, 3);
-- Suite paragraphe 1
INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(2, 1, 'Vas devant l''entrée de la grotte pour commencer ton enquête', 'Tu es devant l''entrée de la grotte de Dragono. Ton ami dragon espère que tu y trouveras des indices laissés par le voleur.',
						1, 2, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 1, 2);
-- Suite paragraphe 2
INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(3, 1, 'Entre dans la grotte pour chercher des indices.', 'Tu allumes ta torche avec ton briquet à silex et commences à avancer dans la grotte. Les parois sont d''un noir luisant avec des reflets rougeoyants. Plus tu avances, plus le sol et le plafond se couvrent de stalagmites et de stalactites qui rendent la progression difficile (pour qui ne sait pas voler!) et donnent l''impression de marcher dans la gueule d''une immense bête féroce. Sur la pointe d''un stalagmite, tu trouves un lambeau de tissus marron.  Tu le prends et le mets dans ta sacoche.',
						1, 3, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 2, 3);
-- Suite paragraphe 3	
INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(4, 1, 'Continue d''avancer dans la grotte', 'Tu continues à marcher et les parois se mettent soudain à scintiller avec la lumière de ta torche. En approchant, tu découvres qu''elles recèlent de pierres précieuses. T''arrêtant un instant pour admirer ce spectacle, tu vois une fiole en verre soigneusement posée sur un rocher à côté de toi. La fiole est à moitié remplie d''un liquide orangeâtre. Tu ramasses avec précaution le fragile flacon.',
						1, 2, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 3, 4);
-- Suite paragraphe 4
INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(5, 1, 'Continue d''avancer dans la grotte', 'En reprenant ton chemin, tu constates que les parois de la grotte s''élargissent. Puis, tu vois maintenant le sol se mettre à briller et semble remuer à la façon des reflets de la lune sur les flots d''une rivière lors d''une nuit claire. En t''abaissant, tu t''aperçois que le sol est recouvert de milliers de pièces d''or qui illuminent la grotte à la lueur de ta torche ! Tu es arrivé dans la plus vaste partie de la grotte, c''est l''antre du dragon. Dragono est déjà en train de t''attendre au milieu de sa tanière.',
						1, 1, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 4, 5);
-- Suite paragraphe 5
INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(6, 1, 'Discute avec Dragono', 'Dragono t''explique alors, qu''il a besoin de ce trésor pour vivre et se régénérer. C''est ainsi que survivent les dragons, sans boire ni manger mais pouvant vivre durant des siècles voire des millénaires à condition de dormir chaque nuit près de leur trésor. Dragono peut se passer des trois pièces d''or qui ont disparues, mais il souhaite savoir qui à fait cela et pourquoi. En fouillant au centre de la grotte dans un creux formé par les pièces, tu découvres une plume, dans ce qui semble être le nid du dragon. Le dragon n''avait pas connaissance de ces 3 objets et te les laisse volontiers pour mener ton enquête.',
						1, 3, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 5, 6);
-- Suite paragraphe 6
INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(7, 1, 'Ressort de la grotte', 'Te revoilà devant l''entrée de la grotte avec Dragono. Il remarque que tu n''as pas d''équipement pour mener à bien cette aventure et te conseille de remédier à cela. Que veux-tu faire ?',
						3, 3, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 6, 7);
-- Suite paragraphe 7
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 7, 3);
	
INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(8, 1, 'A l''entrée de la grotte, tu remarques des empreintes de pas', ' Te voilà sur le sentier qui mène à la forêt. Le début du parcours est plutôt plat et te permet d''aller bon train et de profiter du paysage par cette belle journée ensoleillée. Mais bientôt le chemin rétrécie et des passages plus escarpés t''obligent à ralentir ton allure. Tu dois maintenant escalader des rochers enfoncés dans un maquis fait de petits arbustes et buissons épineux qui ne permettent pas de s''écarter de cette étroite piste. Puis, le maquis laisse la place à un paysage d''arbres plus hauts laissant entendre ça et là les bruits de la forêt. 
Un peu plus loin, le sentier s''élargit à nouveau jusqu''à arriver en face d''un énorme rocher qui a la forme d''un visage de géant qui penche sur sa droite comme pour regarder plus bas. A cet endroit, la piste se sépare en deux, l''une à droite et l''autre à gauche...La piste de gauche descend par un raidillon qui semble mener au plus profond de la forêt où la végétation est plus dense et où les rayons du soleil peinent à se frayer un passage. La piste de droite remonte en serpentant sur les flans d''une colline pour rejoindre une clairière verdoyante.  De quel côté vas-tu?',
						3, 1, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 7, 8);

INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(9, 1, 'Tu décides de te rendre au village', '  Te voilà arrivé dans l''auberge du nain !  C''est un endroit sombre, bruyant et malodorant, mais il est fréquenté par toutes sortes de personnages plus ou moins honnêtes qui pourront sûrement te renseigner.

En entrant dans l''auberge, tu parcours rapidement du regard l''ensemble de la salle. Il n''y a qu''une salle de forme carrée avec des murs faits de bois et pierres sombres rappelant la roche des mines de nains situées dans les montagnes voisines. Sur le mur situé à droite, se trouve une fenêtre, trop petite pour laisser entrer la moindre lumière et qui semble avoir été construite par erreur.  Au centre de la pièce, une dizaine de tables avec des hommes et des femmes en tout genre mangeant, buvant, riant et criant pour se faire entendre. Au fond de l''auberge se trouve une grande cheminée avec une marmite et une grosse pièce de viande sur une rôtissoire. Devant la cheminée, un vieil elfe s''affère entre la marmite et une table recouverte de légumes et autres ingrédients plus ou moins appétissants. Sur la gauche, un long bar en bois d''ébène foncé et sculpté de toutes parts avec à chaque extrémité une tête de nain. L''une des têtes grimace et l''autre est en colère. Situé derrière le comptoir se trouve un nain peu souriant avec une énorme barbe. Tu souris en voyant que le nain balaye le zinc avec sa barbe à chaque fois qu''il se penche pour y poser ou prendre une chope de bière. C''est le maître Aubergiste.

La plupart de ces personnes sont sûrement de simples villageois venus se restaurer, mais plusieurs individus retiennent ton attention... ',
						5, 2, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 7, 9);

-- Suite paragraphe 9
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 9, 7);
INSERT INTO Paragraphe(numparag, idhist, titre, texte, idwritter, valide) VALUES
	(10, 1, 'Les 3 vilains', ' Tu t''approches vers les trois voyageurs en leur demandant si tu peux te joindre à eux. Les vilains cessent de ricaner et te dévisagent. Le plus petit des deux hommes, pousse son assiette devant lui et te répond: «Ce serait avec plaisir mais moi et mes camarades devons reprendre la route». Les trois vilains se lèvent en silence et ramassent leurs affaires. Le grand ahuri se baisse alors pour prendre le gros sac et le met sur son épaule. Lorsqu''il passe près de toi, tu remarques à nouveau le trou dans le sac qui correspond parfaitement au lambeau trouvé dans la grotte. Les trois individus sortent ensuite l''auberge.
Que fais-tu? ',
						1, 0);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 9, 10);

INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(11, 1, 'L''aubergiste', 'Tu t''approches vers les trois voyageurs en leur demandant si tu peux te joindre à eux. Les vilains cessent de ricaner et te dévisagent. Le plus petit des deux hommes, pousse son assiette devant lui et te répond: «Ce serait avec plaisir mais moi et mes camarades devons reprendre la route». Les trois vilains se lèvent en silence et ramassent leurs affaires. Le grand ahuri se baisse alors pour prendre le gros sac et le met sur son épaule. Lorsqu''il passe près de toi, tu remarques à nouveau le trou dans le sac qui correspond parfaitement au lambeau trouvé dans la grotte. Les trois individus sortent ensuite l''auberge.
Que fais-tu? ',
						4 , 2, 0);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 9, 11);

INSERT INTO Paragraphe(numparag, idhist, titre, idwritter, valide) VALUES
	(12, 1, 'L''homme pressé', 3, 0);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 9, 12);
	
INSERT INTO Paragraphe(numparag, idhist, titre) VALUES
	(13, 1, 'L''elfe cuisinier');
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 9, 13);
	
-- Suite paragraphe 8
INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(14, 1, 'Prends le raidillon qui descend sur ta gauche', 'Tu t''engages dans ce petit raidillon qui s''enfonce dans la partie la plus basse de la forêt. Au fur et à mesure que tu descends, le joyeux chant des oiseaux laisse la place à un silence pesant parfois rompu par le cri strident d''un corbeau. Plus tu avances, plus la végétation verdoyante disparaît se laissant doucement remplacer par des branches et des arbres morts. Seuls les plus grands arbres persistent et  levant les yeux vers la canopée tu comprends qu''aucun rayons de soleil ne peut traverser la cime de ces arbres qui semblent n''avoir de feuilles que pour empêcher la lumière de passer.  En continuant ta descente dans ce calme angoissant, tu te surprends parfois à regarder de part et d''autre pour t''assurer de ne pas être observé. Lorsque la descente se termine tu découvres un paysage marécageux dans lequel le sentier se jette et disparaît dans des flaques de boue et d''eau stagnante. Devant toi, il n''y a plus aucun chemin.
Que fais-tu? ',
						2, 1, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 8, 14);
	
INSERT INTO Paragraphe(numparag, idhist, titre) VALUES
	(15, 1, 'Prends la piste de droite qui remonte vers la clairière');
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 8, 15);

INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 8, 9);
	
-- Suite paragraphe 14
INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(16, 1, 'Tu continues dans la même direction et tu avances dans les marécages', 'Pénétrant dans les marécages, tu poursuis ta marche dans la même direction. Devant toi, il n''y a plus de sentier seulement un sol boueux parsemé de flaques et bordé par des arbres morts, des souches, des troncs et des branchages qui ne laissent pas d''autres choix que de suivre cette piste plus dégagée. Contournant et enjambant les étendues d''eau stagnante, tu entres dans une légère brume. Une odeur de putréfaction se fait maintenant sentir. Puis, la brume devient brouillard et bientôt tu n''y vois plus à trois pas. Tu essaie d''avancer à tâtons mais tu t''enlises dans la vase jusqu''aux cuisses... Tu parviens à te dégager de la tourbe lorsque la brume se dissipe enfin. Tu regardes autour de toi et tu ne reconnais plus du tout l''endroit d''où tu viens. Tu es perdu au milieu des marécages. Désespéré, tu t''assois sur un tronc d''arbre en réfléchissant. Finalement, ton vieux chien Rodolphe vient te chercher pour rentrer à la maison.',
						0, 3, 1);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 14, 16);
INSERT INTO IsFollowing(idhistparag, numparagpere, numparagfils) VALUES
	(1, 14, 15);
	
-- Deuxième histoire : à publier publique
INSERT INTO Histoire (idhist, titre, idauteur, prive) VALUES
	(3, 'Histoire à publier', 3, 0);
INSERT INTO Paragraphe(numparag, idhist, titre, texte, nbchoix, idwritter, valide) VALUES
	(1, 1, 'début', 'Tout commence à l''époque où les humains vivaient en harmonie avec de nombreuses autres créatures comme les dragons, les elfes, les nains... La magie était alors utilisée par quelques initiés... Durant ces temps oubliés, de nobles guerriers parcouraient le monde à la recherche de richesses et luttaient vaillamment contre des créatures maléfiques. Peut-être es-tu l''un de ces héros...  Ton aventure débute un matin ensoleillé, près de la tanière de ton ami Dragono. Il t''explique qu''il s''est absenté toute la journée d''hier et en rentrant dans sa grotte, il a découvert qu''il lui manquait 3 pièces d''or ! Il aimerait savoir qui a bien pu lui voler les précieuses pièces de son trésor ? Comme tu es son ami, il te demande de l''aider à retrouver ses pièces car il sait que tu es très fort pour ce genre d''enquête.
Voici ce que contient ta sacoche:
-Un briquet à silex
-Une torche
-Une bourse avec 12 pièces de cuivre.',
						1, 1, 1);
	