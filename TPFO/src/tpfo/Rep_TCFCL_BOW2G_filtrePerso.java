package tpfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rep_TCFCL_BOW2G_filtrePerso extends Rep {
	static List<String> catsToKeep = new ArrayList<>(Arrays.asList("v","n", "adj", "adv", "advneg", "clneg"));
			//    ("n", "v", "adj", "adv", "advneg)", "clneg")
		static List<String> lemmasToIgnore = new ArrayList<>(Arrays.asList(
		"être","est", "bref", "haut", "résultat", "oriental", "annoncer",
		"cela", "avant",
		"bientôt", "menu", "autre", 
		"soir", "arrivé", "là", 
		"encore", "patron", "lyonnais", 
		"frire", "point", "mettre", 
		"café", "tartare", "chocolat", 
		"lors", "fille", "sucrer",
		"gâteau","menu_enfant", "boeuf", 
		"lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi", "dimanche", 
		"paver", "cerise", 
		//retrait perso
		"restaurant",
		"table",
		"cadre",
		"dessert",
		"viande"//0.7282
		,"avec"//0.7382
		));
		
		Tokenizer tokenizer;
		Lexicon lex;
		
		public Rep_TCFCL_BOW2G_filtrePerso(Tokenizer tokenizer, Lexicon lex, int maxSize, int minCount) {
		super(tokenizer, lex, maxSize, minCount);
		this.tokenizer = tokenizer;
		this.lex = lex;
		this.fset = new FeatureSet();
		}
		
		public Rep_TCFCL_BOW2G_filtrePerso(Tokenizer tokenizer, Lexicon lex, int maxSize, int minCount,String word) {
			super(tokenizer, lex, maxSize, minCount);
			this.tokenizer = tokenizer;
			this.lex = lex;
			this.fset = new FeatureSet();
			Rep_TCFCL_BOW2G_filtrePerso.lemmasToIgnore.add(word);
		}
		
		@Override
		public int getDimension() {
		return fset.size();
		}
		
		@Override
		public double[] buildFeatures(Review review) {
		double[] vector = null;
		if (fset.isFinalized()) {
		// l'ensemble des traits est dÃ©jÃ  construit, donc lÃ  on construit
		// le vecteur d'un avis
		vector = new double[fset.size()];
		Arrays.fill(vector, 0);
		}
		// T
		List<String> itokens = tokenizer.tokenize(review.text);
		// F
		List<String> ftokens = filter(itokens);
		// C + L
		List<String> tokens = normalize(ftokens);
		// crÃ©er l'ensemble des traits BOW2G
		for (int i = 0; i < tokens.size(); i++) {
		String token = tokens.get(i);
		// BOW : sac-de-mots
		setFeature(token, 1, vector);
		// 2G : bigrammes
		String bigram = getNgram(2, tokens, i);
		setFeature(bigram, 1, vector);
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
		
		
		/////////// Divers traitements pour construire les traits /////////////////
		
		protected List<String> filter(List<String> tokens) {
		List<String> ftokens = new ArrayList<>();
		for (String token : tokens) {
		// garder seulement les noms, adj, verbes, et adv
		// et ignorer certains mots
		if (lex.hasCatIn(token, catsToKeep)
		&& !lex.hasLemmaIn(token, lemmasToIgnore)) {
		ftokens.add(token);
		}
		}
		return ftokens;
		}
		
		// TOK-F-NT-L-BOW-2G
		private List<String> normalize(List<String> tokens) {
		List<String> ntokens = new ArrayList<>();
		for (String token : tokens) {
		List<String> lemmas = lex.getLemmas(token.toLowerCase());
		if (lemmas == null) {
		ntokens.add(token.toLowerCase());
		} else {
		//for(String l:lemmas) {
		ntokens.add(lemmas.get(0));
		//}
		}
		}
		return ntokens;
		}
}
