package tpfo;

import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationLinear;
import org.encog.engine.network.activation.ActivationRamp;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationSoftMax;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

public class NN_H01sigmoidH01sigmoidH01rampH01ramp extends NN {
	public NN_H01sigmoidH01sigmoidH01rampH01ramp(int inputSize, int outputSize) {
        super(inputSize, outputSize);
        // Créer le RN
        net = new BasicNetwork();
        // Linear activation (ne modifie pas la somme pondérée)
        ActivationFunction linear = new ActivationLinear();
        // Ramp activation 
        ActivationFunction ramp = new ActivationRamp(1, 0, 1, 0);

        // Sigmoide
        ActivationFunction sigmoid = new ActivationSigmoid();
        // Tanh
        ActivationFunction tanh = new ActivationTANH();
        // softmax
        ActivationFunction softmax = new ActivationSoftMax();

        // input layer
        net.addLayer(new BasicLayer(null, true, inputSize));
        // hidden layer(s)

        net.addLayer(new BasicLayer(sigmoid, true, (int) (inputSize*0.1)));
        net.addLayer(new BasicLayer(sigmoid, true, (int) (inputSize*0.1)));
        net.addLayer(new BasicLayer(ramp, true, (int) (inputSize*0.1)));
        net.addLayer(new BasicLayer(ramp, true, (int) (inputSize*0.1)));
        
        // ouput layer 
        net.addLayer(new BasicLayer(softmax, false, outputSize));
        // finaliser la création du RN
        net.getStructure().finalizeStructure();
        net.reset(seed);
    }
}
