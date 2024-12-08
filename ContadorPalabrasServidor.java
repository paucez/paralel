import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ContadorPalabrasServidor extends UnicastRemoteObject implements ContadorPalabrasServidorRemoto {
    private final List<ContadorPalabrasClienteRemoto> clientesConectados = new ArrayList<>();
    private JTextArea clientesArea;

    // Constructor
    protected ContadorPalabrasServidor() throws RemoteException {
        super();
        initGUI(); // Inicializa la interfaz gráfica
    }

    // Método para inicializar la interfaz gráfica
    private void initGUI() {
        JFrame frame = new JFrame("Servidor RMI - Contador de Palabras");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        clientesArea = new JTextArea();
        clientesArea.setEditable(false);
        frame.add(new JScrollPane(clientesArea), BorderLayout.CENTER);

        JLabel label = new JLabel("Clientes conectados:");
        frame.add(label, BorderLayout.NORTH);

        frame.setVisible(true);
    }

    // Método para registrar un cliente
    @Override
    public synchronized void registrarCliente(ContadorPalabrasClienteRemoto cliente) throws RemoteException {
        clientesConectados.add(cliente); // Agrega el cliente a la lista
        actualizarListaClientes(); // Actualiza la interfaz gráfica
        System.out.println("Cliente registrado con IP: " + cliente.obtenerIp());
    }

    // Método para obtener la lista de clientes conectados
    @Override
    public synchronized List<String> obtenerClientesConectados() throws RemoteException {
        List<String> ips = new ArrayList<>();
        for (ContadorPalabrasClienteRemoto cliente : clientesConectados) {
            ips.add(cliente.obtenerIp());
        }
        return ips;
    }

    // Método para actualizar la lista de clientes en la interfaz gráfica
    private void actualizarListaClientes() {
        try {
            List<String> clientes = obtenerClientesConectados();
            clientesArea.setText(String.join("\n", clientes)); // Muestra la lista en el JTextArea
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // Métodos restantes (vacíos o implementables)
    @Override
    public void distribuirTareas(String filePath, int numTareas, String metodo) throws RemoteException {
        // Implementar lógica para distribuir tareas
    }

    @Override
    public int contarPalabrasSecuencial(String filePath) throws RemoteException {
        return 0; // Implementar conteo secuencial
    }

    @Override
    public int contarPalabrasConcurrente(String filePath, int numTareas) throws RemoteException {
        return 0; // Implementar conteo concurrente
    }

    @Override
    public int contarPalabras(String filePath, long start, long end) throws RemoteException {
        return 0; // Implementar lógica de conteo
    }

    // Método principal
    public static void main(String[] args) {
        try {
            ContadorPalabrasServidor servidor = new ContadorPalabrasServidor();
            Registry registry = LocateRegistry.createRegistry(1099); // Crea el registro RMI en el puerto 1099
            registry.rebind("ContadorPalabrasServidor", servidor);
            System.out.println("Servidor listo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
