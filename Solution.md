## Solucion problema 1
# Promps
--------------------------------------------
📝 Problema #1: El Videoclub de Don
Duración: Máximo 15 minutos
Don Mario acaba de abrir un videoclub moderno donde los clientes pueden alquilar peliculas fisicas o digitales. El problema es que su sistema anterior era un caos: todos los precios se calculaban igual sin importar el tipo de pelicula o membresia del cliente, y no habia forma de saber que peliculas estaban disponibles en tiempo real.

Tu Mision
Ayuda a Don Mario creando un sistema de alquiler que permita:

Registrar peliculas (fisicas o digitales) con su disponibilidad.
Que el cliente elija X peliculas para alquilar.
Calcular el precio total segun su tipo de membresia:
Basica: precio normal.
Premium: 20% de descuento.
Mostrar al finalizar un recibo con las peliculas, precio por unidad y total.
Peliculas Disponibles
[Fisica] Interestellar - $8.000 - Disponible
[Fisica] El Padrino - $7.000 - No disponible
[Digital] Inception - $5.000 - Disponible
[Digital] Matrix - $6.000 - Disponible
Caso de Ejemplo
Membresia del cliente: Premium
Seleccione peliculas (numeros separados por coma): 1,3

--- RECIBO DE ALQUILER ---
Cliente: Premium
Peliculas:
 - Interestellar (Fisica) - $8.000
 - Inception (Digital) - $5.000
Subtotal: $13.000
Descuento (20%): $2.600
Total a pagar: $10.400
¡Disfrute su pelicula!
Objetivos del Ejercicio
Identificar cual o cuales patrones de diseno utilizar.
Explicar que principios de SOLID se aplican.
Aplicar polimorfismo y encapsulamiento.
Colocar evidencia de la ejecucion del ejercicio (ejecucion por consola; no es necesario hacer front).

ayudame a tener el codigo de este problema y por favor que se cumpla todo de una forma sencilla nada compleja pero cumpliendo los requerimientos.
------------------------------------
no no es una estructura apache maven java
------------------------------------
no deepseek no es necesario que crees todas esas clases ni mucho menos un main porque la estructura maven ya me da una clase Aplication.java que es el @SpringBootApplication
------------------------------------
Codigo
package com.videoclub.model;

public class Pelicula {
    private String titulo;
    private String tipo; // "Fisica" o "Digital"
    private double precio;
    private boolean disponible;
    
    public Pelicula(String titulo, String tipo, double precio, boolean disponible) {
        this.titulo = titulo;
        this.tipo = tipo;
        this.precio = precio;
        this.disponible = disponible;
    }
    
    // Getters
    public String getTitulo() { return titulo; }
    public String getTipo() { return tipo; }
    public double getPrecio() { return precio; }
    public boolean isDisponible() { return disponible; }
}

package com.videoclub.model;

public enum TipoMembresia {
    BASICA {
        @Override
        public double aplicarDescuento(double subtotal) {
            return subtotal;
        }
    },
    PREMIUM {
        @Override
        public double aplicarDescuento(double subtotal) {
            return subtotal * 0.8; // 20% descuento
        }
    };
    
    public abstract double aplicarDescuento(double subtotal);
}

package com.videoclub.model;

public class Membresia {
    private TipoMembresia tipo;
    
    public Membresia(TipoMembresia tipo) {
        this.tipo = tipo;
    }
    
    public double calcularTotal(double subtotal) {
        return tipo.aplicarDescuento(subtotal);
    }
    
    public String getNombre() {
        return tipo.name();
    }
}

package com.videoclub.service;

import com.videoclub.model.Pelicula;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AlquilerService {
    
    // Catálogo de películas (simulando BD)
    private final List<Pelicula> catalogo = Arrays.asList(
        new Pelicula("Interestellar", "Fisica", 8000, true),
        new Pelicula("El Padrino", "Fisica", 7000, false),
        new Pelicula("Inception", "Digital", 5000, true),
        new Pelicula("Matrix", "Digital", 6000, true)
    );
    
    public List<Pelicula> getCatalogo() {
        return catalogo;
    }
    
    public List<Pelicula> seleccionarPeliculas(List<Integer> indices) {
        List<Pelicula> seleccionadas = new ArrayList<>();
        for (int indice : indices) {
            if (indice >= 1 && indice <= catalogo.size()) {
                Pelicula p = catalogo.get(indice - 1);
                if (p.isDisponible()) {
                    seleccionadas.add(p);
                }
            }
        }
        return seleccionadas;
    }
    
    public double calcularSubtotal(List<Pelicula> peliculas) {
        return peliculas.stream().mapToDouble(Pelicula::getPrecio).sum();
    }
}

package com.videoclub.service;

import com.videoclub.model.Pelicula;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReciboService {
    
    public void generarRecibo(String tipoMembresia, List<Pelicula> peliculas, 
                              double subtotal, double total) {
        double descuento = subtotal - total;
        
        System.out.println("\n--- RECIBO DE ALQUILER ---");
        System.out.println("Cliente: " + tipoMembresia);
        System.out.println("Peliculas:");
        peliculas.forEach(p -> 
            System.out.printf(" - %s (%s) - $%.0f%n", p.getTitulo(), p.getTipo(), p.getPrecio())
        );
        System.out.printf("Subtotal: $%.0f%n", subtotal);
        if (descuento > 0) {
            System.out.printf("Descuento (20%%): $%.0f%n", descuento);
        }
        System.out.printf("Total a pagar: $%.0f%n", total);
        System.out.println("--------------------------");
        System.out.println("¡Disfrute su pelicula!");
    }
}

package com.videoclub.controller;

import com.videoclub.model.*;
import com.videoclub.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import java.util.*;

@Controller
public class AlquilerController implements CommandLineRunner {
    
    @Autowired
    private AlquilerService alquilerService;
    
    @Autowired
    private ReciboService reciboService;
    
    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        
        // Mostrar catálogo
        System.out.println("\n--- CATÁLOGO DE PELÍCULAS ---");
        List<Pelicula> catalogo = alquilerService.getCatalogo();
        for (int i = 0; i < catalogo.size(); i++) {
            Pelicula p = catalogo.get(i);
            String estado = p.isDisponible() ? "Disponible" : "No disponible";
            System.out.printf("%d. %s (%s) - $%.0f - %s%n", 
                i+1, p.getTitulo(), p.getTipo(), p.getPrecio(), estado);
        }
        
        // Seleccionar membresía
        System.out.println("\nSeleccione su tipo de membresía:");
        System.out.println("1. Básica");
        System.out.println("2. Premium");
        System.out.print("Opción: ");
        int opcion = scanner.nextInt();
        
        TipoMembresia tipo = (opcion == 2) ? TipoMembresia.PREMIUM : TipoMembresia.BASICA;
        Membresia membresia = new Membresia(tipo);
        
        // Seleccionar películas
        System.out.print("\nSeleccione películas (números separados por coma): ");
        scanner.nextLine();
        String[] indicesStr = scanner.nextLine().split(",");
        List<Integer> indices = Arrays.stream(indicesStr)
            .map(s -> Integer.parseInt(s.trim()))
            .toList();
        
        // Procesar alquiler
        List<Pelicula> seleccionadas = alquilerService.seleccionarPeliculas(indices);
        
        if (seleccionadas.isEmpty()) {
            System.out.println("No se seleccionó ninguna película válida.");
            return;
        }
        
        double subtotal = alquilerService.calcularSubtotal(seleccionadas);
        double total = membresia.calcularTotal(subtotal);
        
        // Generar recibo
        reciboService.generarRecibo(membresia.getNombre(), seleccionadas, subtotal, total);
        
        scanner.close();
    }
}

Para hacer este codigo de maven del primer punto me demore unos 8 minutos. 
Muchas gracias a Ignacio por no aclarar que no necesariamente tenia que ser en maven :v me hizo perder el tiempo mlp.