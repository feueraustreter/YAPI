package yapi.neuralnet;

import yapi.neuralnet.frontend.NeuralNetBuilder;

public class Test {

    public static void main(String[] args) {
        NeuralNetBuilder neuralNetBuilder = new NeuralNetBuilder();
        neuralNetBuilder.edit().add(9).add(5).add(5).add(5).add(9);
        neuralNetBuilder.build();
    }

}
