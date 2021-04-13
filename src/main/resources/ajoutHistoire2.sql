INSERT INTO UTILISATEUR(idUtil, nom, prenom, email, password) VALUES(2, 'JEZEQUEL-ROYER', 'Louis', 'louis.jezequel-royer@grenoble-inp.org', 'code');

INSERT INTO Histoire(idHist, titre, prive, datePubli, idAuteur) VALUES(2, 'Monsieur Charles', 0, SYSDATE, 2);

INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES (1, 'Le commencement', 'Aujourd''hui, comme tous les lundis, dès la sortie de la classe, je cours vers le parc avec mes copains pour y retrouver monsieur Charles. J''essaye d''arriver le premier pour pouvoir choisir l''histoire qu''il va nous conter. Moi, je choisis toujours des histoires de détectives... Vite je dois me dépêcher...', 1, 1, 2, 2);

INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES(2, 'Le parc', 'Nous voilà arrivés au parc. On repère toujours monsieur Charles de loin grâce à son grand panier rouge. Mais ce soir, le banc vert sur lequel monsieur Charles s’assoit est vide. Nous partons à sa recherche dans le parc...', 1, 3, 2, 2);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 1, 2);
INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES(3, 'Nous partons vers le grand arbre','Nous avons beau chercher, mais nous ne le trouvons toujours pas. Nous décidons de retourner vers le banc vert. ' ,1, 1, 2, 2);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 2, 3);
INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES(4, 'Nous allons vers le petit ruisseau', 'Il n’y a personne du côté du petit ruisseau. Nous décidons de retourner vers le banc vert. Pour aller plus vite nous décidons de revenir par le petit talus.> Passez par le petit talus.', 1, 1, 2, 2);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 2, 4);
INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES(5, 'S''asseoir sur le banc', 'Nous nous asseyons sur le banc et attendons un peu. Mais personne ne vient. Pour la première fois, nous n’aurons pas d’histoire...', 1, 2, 2, 2);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 3, 5);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 4, 5);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 5, 2);
INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist, conditionParag) VALUES(6, 'Nous partons vers l''entrée du parc', 'Arrivés à l’entrée du parc, nous voyons un petit monsieur avec un manteau gris. C’est lui!!! Nous l’appelons « Monsieur Charles!!!» Mais il ne se retourne pas: ce n’était pas lui... nous décidons de retourner vers le banc vert. ', 1, 1, 2, 2, 4);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 2, 6);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 6, 5);
INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES(7, 'Rentrer à la maison', 'Nous nous dirigeons vers la sortie du parc. Tout à coup Fabien crie: « Regardez, regardez, c’est le panier de monsieur Charles! ». Il montrait une dame qui portait le panier rouge de notre ami. Tout ceci est bien mystérieux.', 1, 2, 2, 2);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 5, 7);
INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES(8, 'Jouer à la GameBoy à la maison', 'Nous rentrons tous à la maison, on reviendra lundi prochain pour voir s’il sera là... ', 1, 1, 2, 2);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 8, 1);
INSERT INTO PARAGRAPHE(numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES(9, 'Suivre la dame', 'Nous suivons la dame. Lucie avait un peu peur, mais je l’ai encouragée: nous devons savoir ce qui est arrivé à monsieur Charles. Nous courons jusqu’à la grille. A suivre...', 1, 0, 2, 2);
INSERT INTO isFollowing(idHistParag, numParagPere, numParagFils) VALUES(2, 7, 9);

