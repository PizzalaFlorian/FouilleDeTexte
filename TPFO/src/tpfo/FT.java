/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfo;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import static java.lang.System.out;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Salah Ait-Mokhtar
 */
public class FT {

    public static Evaluation experiment(Rep rep, NN nn,
            Dataset trainSet, Dataset testSet) throws FileNotFoundException, UnsupportedEncodingException {
        // afficher l'ensemble des traits
        out.println(rep.fset);
        PrintWriter writer = new PrintWriter("/home/florian/workspace/fouilleTexte/TPFO/resultats/traits.txt", "UTF-8");
        writer.println(rep.fset);
        writer.close();
        
        Model model = new Model(rep, nn);
        // entraîner sur les données d'entraînements
        model.train(trainSet);
        // Evaluer sur le données de test
        Evaluation eval = new Evaluation();
        eval.evaluate(model, testSet);
        // retourner les résultats d'évaluation
        return eval;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // 
        //String lexPathname = "../../resources/lefff-3.4.mlex";
        String lexPathname ="D:/workspace/FouilleDeTexte/FouilleDeTexte/TPFO/resources/lefff-3.4.mlex";
        //String corpusPathname = "../../Corpus/corpus.all14";
<<<<<<< HEAD
        String corpusPathname = "D:/workspace/FouilleDeTexte/FouilleDeTexte/TPFO/corpus/corpus.all20";
=======
        String corpusPathname = "/home/florian/workspace/fouilleTexte/TPFO/corpus/corpus.all20";
>>>>>>> 7d68a834160c99462dcf358b95d74b44b02eeac6
        if (args.length == 2) {
            lexPathname = args[0];
            corpusPathname = args[1];
        }
        // charger le lexique
        Lexicon lex = new Lexicon();
        lex.load(Paths.get(lexPathname));
        // Créer un tokeniseur
        Tokenizer tokenizer = new Tokenizer(lex);
        // Charger les données
        Path dataPath = Paths.get(corpusPathname);
        Dataset dataset = Dataset.load(dataPath);
        // Réserver 80% pour l'entraînement, et 20% pour le test
        Dataset testset = dataset.split(0.80f);

<<<<<<< HEAD
        // Créer une représentation
        Rep rep = new Rep_TCF_BOW(tokenizer, lex, 500, 50);
//        Rep rep = new Rep_TCFL_BOW(tokenizer, lex, 700, 5);
//        Rep rep = new Rep_TCFL_BOW2G(tokenizer, lex, 700, 5);
=======
        //****************************** Créer une représentation **************************
        //Rep rep = new Rep_TCFL_BOW(tokenizer, lex, 700, 5);
        Rep rep = new Rep_TFCL_BOW2G(tokenizer, lex, 500, 5); //bigrammes
        //bow = bag of word 
        //L = lématisation
>>>>>>> 7d68a834160c99462dcf358b95d74b44b02eeac6
        // initialiser la représentation (l'ensemble de ses traits)
        //TODO ajouter les nouvelles representations perso;
        rep.initializeFeatures(dataset);
        
        //************************************* Créer un RN *********************************
//        NN nn = new NN_H15tanh(rep.getDimension(), ClassLabel.size());
        NN nn = new NN_H15tanhH05tanh(rep.getDimension(), ClassLabel.size());
//        NN nn = new NN_H90tanhH20tanh(rep.getDimension(), ClassLabel.size());
        //TODO ajouter des nouveaux réseaux de neurones perso;
        //NeuralNetwork_H(hidden)+TailleCoucheCaché+FctionActivation+H(hidden)+Taillecouchecaché+fctionActivation2
        // lancer l'expérience
        Evaluation eval = experiment(rep, nn, dataset, testset);
        
        //******************************** Afficher les résultats*****************************
        //System.out.println(eval.resultToString());
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
		                new FileOutputStream("/home/florian/workspace/fouilleTexte/TPFO/resultats/res.txt"), "utf-8"))) {
		     writer.write(eval.resultToString());
		  }
        System.out.println("done");
    }

}
