/*
 * Una empresa de análisis de datos en Twitter desea mostrar gráficas de
 *  sus encuestas realizadas.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

class VentanaInicio extends JFrame{
	Random rm = new Random();
	ArrayList datos = new ArrayList();
	JTextArea cajaSI, cajaNO;

	public VentanaInicio() {
		getContentPane().setBackground(new Color (213, 229, 213) );
		setSize(400, 320);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setTitle("Análisis de datos en Twitter");
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);


		JLabel txtSI = new JLabel("SI");
		txtSI.setBounds(105,0,150,15);
		add(txtSI);

		JLabel txtNO = new JLabel("NO");
		txtNO.setBounds(275,0,200,15);
		add(txtNO);

		cajaSI = new JTextArea();
		cajaSI.setBackground(new Color (199, 217, 221));
		cajaSI.setBounds(0,25,200,100);
		add(cajaSI);

		cajaNO = new JTextArea();
		cajaNO.setBackground(new Color (199, 217, 221));
		cajaNO.setBounds(200,25,200,100);
		add(cajaNO);


		for(int i=0; i< 10; i++) {
			int random= rm.nextInt(2);
			if(random ==0) {
				datos.add("Dato NO."+(i+1)+": NO");
			}else {
				datos.add("Dato NO."+(i+1)+": SI");
			}
		}
		//for(int i=0; i< 10; i++) {	System.out.println(datos.get(i)+"rrr");}
	}
}
class HiloAnalisis extends  Thread{
	VentanaInicio v ;
	public HiloAnalisis(VentanaInicio v) {
		this.v=v;
	}

	public void run() {
		System.out.println("INICIO_ HILO Analisis");
		ArrayList e = v.datos;
		String textoSI = "";
		String textoNO = "";
		for(int i =0; i< v.datos.size(); i++) {
			if(e.get(i).equals("Dato NO."+(i+1)+": NO")) {
				System.out.println("que no");
				textoNO += e.get(i) + "\n";//se lo agregamos para que no se imprima todo en una linea 
			}else {
				System.out.println("que si");
				textoSI += e.get(i) + "\n";
			}
		}
		v.cajaSI.setText(textoSI);
		v.cajaNO.setText(textoNO);
		System.out.println("FINAL_ HILO Analisis");
	}}
class HiloHistograma extends  Thread{
	VentanaInicio v ;
	public HiloHistograma(VentanaInicio v) {
		this.v=v;
	}
	JProgressBar bar = new JProgressBar(0,100);//tamaño del vaso de agua 

	public void run() {
		
	//UTILIZAR EL JPROGRESBAR	/////////////////////////////////////////
		
	bar.setBounds(100,130,200,20);// posicion del vaso de agua
	//bar.setValue(50);//el agua que va a contener el vaso de agua
	bar.setStringPainted(true);//Tenemos que poner esto para que sea visible el bar.setString(i+"%");

	bar.setForeground(Color.BLUE); //Pintamos el agua
	v.repaint();//para que se pueda ver el vaso de agua sin maximizar la ventana
	v.getContentPane().add(bar);//agregar vaso de agua a la interfaz
	//UTILIZAR EL JPROGRESBAR	////////////////////////////////////////////////////
	for (int i = 0; i <= 100; i++) {//el agua que se le pondra al baso 
		bar.setValue(i);
		bar.setString(i + "%"); 
		
		//Esta parte hace que el agua suba de volumen en 5 segundos con el Thread sleep/***************************
		try {
	        Thread.sleep(10);
	    } catch (InterruptedException ex) {
	        Thread.currentThread().interrupt();
	    }//********************************************************************************************************
	}
	

	JPanel panelHistograma = new JPanel() {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);//
			ArrayList e = v.datos;
			int si = 0;
			int no = 0;

			// Contar los valores
			for (Object dato : e) {
				if (dato.toString().contains("SI")) {
					si++;
				} else {
					no++;
				}
			}

			// Dibujar las barras del histograma
			int anchoBarra = 50;
			int altoBarra = 100;
			int espacioEntreBarras = 20;

			g.setColor(Color.BLUE);
			g.fillRect(10, getHeight() - si * altoBarra / 10, anchoBarra, si * altoBarra / 10);
			g.drawString("SI", 10, getHeight() - si * altoBarra / 10 - 10);

			g.setColor(Color.RED);
			g.fillRect(10 + anchoBarra + espacioEntreBarras, getHeight() - no * altoBarra / 10, anchoBarra, no * altoBarra / 10);
			g.drawString("NO", 10 + anchoBarra + espacioEntreBarras, getHeight() - no * altoBarra / 10 - 10);
		}
	};
	panelHistograma.setBounds(100, 150, 200, 200);
	v.getContentPane().add(panelHistograma);
	v.revalidate();
	v.repaint();
	
	}
}
public class Twitter {

	public static void main(String[] args) {
		VentanaInicio ventana = new VentanaInicio();

		HiloAnalisis t1 = new HiloAnalisis(ventana);
		

		HiloHistograma t2 = new HiloHistograma(ventana);
		t1.start();
		t2.start();

	}

}
