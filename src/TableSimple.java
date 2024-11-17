import java.util.Vector;

public class TableSimple implements Operateur{
	
	private Vector<Tuple> contenu;
	int compteur = 0;
	int taille = 0;
	int range = 5;
	
	public TableSimple() {
		this.contenu = new Vector<Tuple>();
	}
	
	public void randomize(int tuplesize, int tablesize) {
		for(int i=0;i<tablesize;i++) {
			Tuple t = new Tuple(tuplesize);
			for(int j=0;j<tuplesize;j++) {
				t.val[j]=(int)(Math.random()*this.range);
			}
			this.contenu.add(t);
		}
		this.taille = this.contenu.size();
	}
	
	public void open() {
		this.compteur = 0;
	}
	
	public Tuple next() {
		if(this.compteur<this.taille)
			return(this.contenu.elementAt(this.compteur++));
		else
			return null;
	}
	
	public void close(){
		
	}

}
