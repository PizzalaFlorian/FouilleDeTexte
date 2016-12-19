/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfo;

import java.io.IOException;
import static java.lang.System.out;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Salah Ait-Mokhtar
 */
public class FT {

    public static Evaluation experiment(Rep rep, NN nn,
            Dataset trainSet, Dataset testSet) {
        // afficher l'ensemble des traits
        out.println(rep.fset);
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
        String lexPathname = "C:/Users/Ga�tan/Eclipse/workspace/TEXTE/resources/lefff-3.4.mlex/lefff-3.4.mlex";
        String corpusPathname = "C:/Users/Ga�tan/Eclipse/workspace/TEXTE/corpus/corpus.all20";
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

        // Créer une représentation
//        Rep rep = new Rep_TCF_BOW(tokenizer, lex, 700, 5);
//        Rep rep = new Rep_TCFL_BOW(tokenizer, lex, 700, 5);
//        Rep rep = new Rep_TCF_BOW2G(tokenizer, lex, 700, 5);
        Rep rep = new Rep_TFCL_BOW2G(tokenizer, lex, 500, 20);
        // initialiser la représentation (l'ensemble de ses traits)
        rep.initializeFeatures(dataset);
        // Créer un RN
        NN nn = new NN_H10tanhH01tanh(rep.getDimension(), ClassLabel.size());
//        NN nn = new NN_H15tanh(rep.getDimension(), ClassLabel.size());
//        NN nn = new NN_H15tanhH05tanh(rep.getDimension(), ClassLabel.size());
//        NN nn = new NN_H50tanhH10tanh(rep.getDimension(), ClassLabel.size());
//        NN nn = new NN_H90tanhH20tanh(rep.getDimension(), ClassLabel.size());
        // lancer l'expérience
        Evaluation eval = experiment(rep, nn, dataset, testset);
        // Afficher les résultats
        System.out.println(eval.resultToString());

    }

}
