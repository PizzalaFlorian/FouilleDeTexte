package tpfo;

import java.util.Arrays;
import java.util.List;

/**
 * T = Tokenisation
 * F = Filtrage de certains mots
 * C = Casse (normalisation de la casse)
 * L = Lemmatisation
 * BOW2G = BOW (Bag-of-words = sacs-de-mots) + 2G (bi-grammes)
 * 
 * @author Gaetan Romagna
 */
public class Rep_TCFL_BOW_perso extends Rep {

	public Rep_TCFL_BOW_perso(Tokenizer tokenizer, Lexicon lex, int maxSize, int minCount) {
		super(tokenizer, lex, maxSize, minCount);
	}
	
	@Override
	public int getDimension() {
		return fset.size();
	}
	
	@Override
	public double[] buildFeatures(Review review) {
        double[] vector = null;
        if (fset.isFinalized()) {
        	//retourne la taille des trait (si indiqué 700 = 700)
            vector = new double[fset.size()];
            Arrays.fill(vector, 0);
        }
        // T
        List<String> itokens = tokenizer.tokenize(review.text);
        // C
        List<String> ctokens = normalizeCase(itokens);
        // F
        List<String> ftokens = filter(ctokens);
        // L
        List<String> tokens = lemmatize(ftokens);
        // creer l'ensemble des traits BOW
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            // BOW : sac-de-mots
            setFeature(token, 1, vector);
        }
        return vector;
	}

	@Override
	public void initializeFeatures(Dataset trainDataset) {
        for (Review review : trainDataset) {
            buildFeatures(review);
        }
        fset.selectByCount(minCount);
        fset.selectBySize(maxSize);
        fset.setFinalized(true);
	}



}
