package tp.boundaries;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class StartPage implements ActionListener {
        private JButton btnEntrar, btnFechar;
        private JPanel panel;
        private Game game;
        private JLabel labelPrincipal, labelNome, labelServidor, labelPorta;
        private JTextField txtNome, txtIP, txtPorta;
        private JRadioButton btnServ, btnNovoServ;
        private ButtonGroup group;
        
        public StartPage(Game g)
        {
                this.game = g;
                panel = new JPanel();
                panel.setLayout(null);
                panel.setSize(Game.WIDTH, Game.HEIGHT);
                
                labelPrincipal = new JLabel(Game.NAME);
                labelPrincipal.setFont(new Font("SansSerif", Font.PLAIN, 50));
                labelPrincipal.setHorizontalAlignment(SwingConstants.CENTER);
                labelPrincipal.setBounds(10, 10, Game.WIDTH - 20, 50);
                labelPrincipal.setForeground(Color.BLACK);
                panel.add(labelPrincipal);
                
                labelNome = new JLabel("Nome: ");
                labelNome.setFont(new Font("SansSerif", Font.PLAIN, 16));
                labelNome.setBounds(10, labelPrincipal.getHeight() + labelPrincipal.getY() + 20, 100, 20);
                panel.add(labelNome);
                
                txtNome = new JTextField();
                txtNome.setBounds(10, labelNome.getHeight() + labelNome.getY() + 5, Game.WIDTH - 10, 25);
                panel.add(txtNome);
                
                labelServidor = new JLabel("Servidor: ");
                labelServidor.setFont(new Font("SansSerif", Font.PLAIN, 16));
                labelServidor.setBounds(10, txtNome.getHeight() + txtNome.getY() + 10, 100, 20);
                panel.add(labelServidor);
                
                btnServ = new JRadioButton();
                btnServ.setBounds(10, labelServidor.getHeight() + labelServidor.getY() + 5, 20, 25);
                btnServ.setSelected(true);
                btnServ.addActionListener(this);
                panel.add(btnServ);
                
                txtIP = new JTextField("localhost");
                txtIP.setBounds(btnServ.getX() + btnServ.getWidth() + 3, btnServ.getY(), Game.WIDTH - btnServ.getWidth() - 100, 25);
                panel.add(txtIP);
                
                labelPorta = new JLabel("Porta: ");
                labelPorta.setFont(new Font("SansSerif", Font.PLAIN, 16));
                labelPorta.setBounds(txtIP.getX() + txtIP.getWidth() + 5, labelServidor.getY(), 80, 20);
                panel.add(labelPorta);
                
                txtPorta = new JTextField("1331");
                txtPorta.setBounds(labelPorta.getX(), txtIP.getY(), 80, 25);
                panel.add(txtPorta);
                
                btnNovoServ = new JRadioButton("Criar novo servidor");
                btnNovoServ.setBounds(10, btnServ.getHeight() + btnServ.getY() + 5, 150, 25);
                btnNovoServ.addActionListener(this);
                panel.add(btnNovoServ);
                
                group = new ButtonGroup();
            group.add(btnServ);
            group.add(btnNovoServ);
                
                btnEntrar = new JButton("Entrar");
                btnEntrar.setBounds(10, Game.HEIGHT - 30, 100, 30);
                btnEntrar.addActionListener(this);
                panel.add(btnEntrar);
                
                btnFechar = new JButton("Fechar");
                btnFechar.setBounds(Game.WIDTH - 100, Game.HEIGHT - 30, 100, 30);
                btnFechar.addActionListener(this);
                panel.add(btnFechar);
                
                game.frame.add(panel, BorderLayout.CENTER);
                game.setVisible(false);
                
        }

        @Override
        public void actionPerformed(ActionEvent e) {
                if(e.getSource() == btnEntrar){
                        String name = txtNome.getText();
                        String serv = txtIP.getText();
                        String port = txtPorta.getText();
                        if(name.length() == 0) {
                                JOptionPane.showMessageDialog(game.frame, "Digite o nome!");
                                txtNome.grabFocus();
                        } else {
                                if(btnServ.isEnabled()) {
                                        if(serv.length() == 0) {
                                                JOptionPane.showMessageDialog(game.frame, "Digite o IP!");
                                                txtIP.grabFocus();
                                        }
                                }
                                try {
                                        this.game.porta = Integer.parseInt(port);
                                        if(this.game.porta < 1024 || this.game.porta > 49152) {
                                                JOptionPane.showMessageDialog(game.frame, "A porta deve estar entre 1024 e 49152!");
                                                txtPorta.grabFocus();
                                        } else {
                                                this.game.username = name;
                                                this.game.iniciaServidor = btnNovoServ.isSelected();
                                                this.game.ip = serv;
                                                this.panel.setVisible(false);
                                                this.game.action();
                                        }
                                } catch (Exception ex) {
                                        JOptionPane.showMessageDialog(game.frame, "Digite a porta corretamente!");
                                        txtPorta.grabFocus();
                                }
                        }
                } else if(e.getSource() == btnFechar) {
                        System.exit(0);
                } else if(e.getSource() == btnServ) {
                        txtIP.setEnabled(true);
                        txtPorta.setBounds(txtPorta.getX(), btnServ.getY(), txtPorta.getWidth(), txtPorta.getHeight());
                } else if(e.getSource() == btnNovoServ) {
                        txtIP.setEnabled(false);
                        txtPorta.setBounds(txtPorta.getX(), btnNovoServ.getY(), txtPorta.getWidth(), txtPorta.getHeight());
                }
        }
}
