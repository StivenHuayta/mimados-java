package pe.pucp.progra3.mimados.model.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.Duration;

public class Horario {
        private int id;
        private Empleado empleado;
        private DayOfWeek diaSemana;
        private LocalTime horaInicio;
        private LocalTime horaFin;
        private boolean activo;
        private Sede sede;

        public Horario() { }

        public Horario(Empleado empleado, DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin , Sede sede) {
            if (empleado == null) {
                throw new IllegalArgumentException("El horario debe pertenecer a un empleado.");
            }
            if (horaInicio == null || horaFin == null) {
                throw new IllegalArgumentException("Las horas de inicio y fin son obligatorias.");
            }

            if(sede == null){
                throw new IllegalArgumentException("El horario debe pertenecer a una sede.");
            }

            if (!horaInicio.isBefore(horaFin)) {
                throw new IllegalArgumentException("La hora de inicio debe ser estrictamente menor a la hora de fin.");
            }

            this.empleado = empleado;
            this.diaSemana = diaSemana;
            this.horaInicio = horaInicio;
            this.horaFin = horaFin;
            this.sede = sede;
            this.activo = true;
        }

        public Horario(int id, Empleado empleado, DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin, boolean activo , Sede sede) {
            this(empleado, diaSemana, horaInicio, horaFin , sede); // Reutiliza la validaciÃ³n de horas
            this.id = id;
            this.activo = activo;
        }


        public boolean cubreLaHora(LocalTime horaSolicitada) {
            if (!this.activo) return false;

            // Retorna true si la hora solicitada es igual o despuÃ©s del inicio, Y antes del fin
            return !horaSolicitada.isBefore(this.horaInicio) && horaSolicitada.isBefore(this.horaFin);
        }


        public long calcularHorasDeTurno() {
            return Duration.between(this.horaInicio, this.horaFin).toHours();
        }

        public void desactivar() {
            this.activo = false;
        }



        //-----------------------------------------------------


        public int getId() { return id; }
        public Empleado getEmpleado() { return empleado; }
        public Sede getSede(){return sede; }
        public DayOfWeek getDiaSemana() { return diaSemana; }
        public LocalTime getHoraInicio() { return horaInicio; }
        public LocalTime getHoraFin() { return horaFin; }
        public boolean isActivo() { return activo; }



}

