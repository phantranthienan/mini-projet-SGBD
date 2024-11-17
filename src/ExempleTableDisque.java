
public class ExempleTableDisque {

	public static void main(String[] args) {
		TableDisque T4 = new TableDisque();
		T4.setFilePath("D:/Study-Work/Java Avance/mini-projet SGBD/src/table1"); // Mettez ici le bon chemin vers votre fichier
		TableDisque T5 = new TableDisque();
		T5.setFilePath("D:/Study-Work/Java Avance/mini-projet SGBD/src/table2"); // idem
		Tuple t = null;
	

		T4.open();
		T5.open();
		System.out.println("Table T4 ****");
		while((t = T4.next())!=null)
			System.out.println(t);
		System.out.println("Table T5 ****");
		while((t = T5.next())!=null)
			System.out.println(t);
		T4.close();
		T5.close();
		
		FiltreEgalite f = new FiltreEgalite(T4, 2, 1);
		f.open();
		System.out.println("Filtre sur T4 ****");
		while((t = f.next())!=null)
			System.out.println(t);
		f.close();
		
		DBI join = new DBI(T4, T5, 0, 0);
		join.open();
		System.out.println("JOIN ****");
		while((t= join.next())!=null)
			System.out.println(t);
	
	}


}
