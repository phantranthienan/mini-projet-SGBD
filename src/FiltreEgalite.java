
public class FiltreEgalite implements Operateur{
	
	private Operateur in;
	private int col;
	private int val;
	
	// Operateur qui produit les tuples vérifiant attribut _col = _val
	public FiltreEgalite(Operateur _in, int _col, int _val){
		this.in = _in;
		this.col = _col;
		this.val = _val;
	}

	@Override
	public void open() {
		this.in.open();
	}

	@Override
	public Tuple next() {
		Tuple t = null;
		while((t=(this.in.next()))!=null){
			if(t.val[this.col]==this.val){
				return t;
			}
			else{
				return this.next();
			}
		}
		return t;
	}

	@Override
	public void close() {
		this.in.close();		
	}

}
