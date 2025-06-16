package com.plast.cash.listener;

import com.fazecast.jSerialComm.SerialPort;
import com.plast.cash.user.UserService;
import com.plast.cash.user.dto.ReciclagemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class SerialListener implements CommandLineRunner {

    private SerialPort comPort;
    private OutputStream out;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final UserService service;
    private final Pattern pattern = Pattern.compile("CPF: (\\d+) - Garrafas: (\\d+)");


    @Override
    public void run(String... args) {
        executor.submit(() -> {

            SerialPort[] ports = SerialPort.getCommPorts();
            System.out.println("Portas disponíveis:");
            for (SerialPort port : ports) {
                System.out.println(" - " + port.getSystemPortName());
            }

            String portName = "ttyUSB0";

            for (SerialPort port : ports) {
                if (port.getSystemPortName().equals(portName)) {
                    comPort = port;
                    break;
                }
            }

            if (comPort == null) {
                System.err.println("Porta " + portName + " não encontrada!");
                return;
            }

            comPort.setBaudRate(9600);
            comPort.setNumDataBits(8);
            comPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
            comPort.setParity(SerialPort.NO_PARITY);
            comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);

            if (!comPort.openPort()) {
                System.err.println("Não foi possível abrir a porta " + portName);
                return;
            }

            out = comPort.getOutputStream();

            System.out.println("Porta " + portName + " aberta. Lendo dados...");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(comPort.getInputStream()))) {

                String line;
                while (true) {

                    try {
                        line = reader.readLine();

                        if (line != null) {

                            Matcher matcher = pattern.matcher(line);

                            if (matcher.find()) {
                                String cpf = matcher.group(1);
                                String garrafas = matcher.group(2);

                                try {
                                    ReciclagemDTO reciclagemDTO = new ReciclagemDTO(cpf, Integer.valueOf(garrafas));
                                    service.saveReciclagem(reciclagemDTO);
                                } catch (Exception e) {
                                    System.err.println("Erro ao salvar reciclagem: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            }

                            System.out.println("Arduino: " + line);
                        } else {
                            System.err.println("Stream fechada, saindo do loop.");
                            break;
                        }

                    } catch (com.fazecast.jSerialComm.SerialPortTimeoutException ignored) {}
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (comPort.isOpen()) {
                    comPort.closePort();
                    System.out.println("Porta fechada.");
                }
            }
        });
    }

    public synchronized void enviarMensagem(String msg) throws Exception {

        if (out == null) {
            System.out.println("OutPut não inicializado");
            return;
        }

        if (!msg.endsWith("\n")) {
            msg += "\n";
        }

        out.write(msg.getBytes());
        out.flush();
        System.out.println("Mensagem: " + msg.trim());
    }
}
