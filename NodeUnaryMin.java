public class NodeUnaryMin extends NodeFact {
    // private Double unop;
    // public NodeUnaryMin(int pos, String unop) {
    //     this.pos = pos;
    //     this.unop = Double.parseDouble(unop);
    // }
    // public double eval(double o1) throws EvalException {
    
    //         return -o1;
    // }
    private NodeFact test = null;
        public NodeUnaryMin(NodeFact un) {
            this.test = un;
        }
        public double eval(Environment o1) throws EvalException
        {
        return -test.eval(o1);
        }
    }
