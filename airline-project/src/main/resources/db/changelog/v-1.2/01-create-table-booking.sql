-- changeset aleksandr_pekarskiy:1.2.1.1
CREATE TABLE booking (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, booking_data_time TIMESTAMP WITHOUT TIME ZONE, booking_number VARCHAR(255) NOT NULL, flight_id BIGINT, flight_seat_id BIGINT, passenger_id BIGINT, CONSTRAINT "bookingPK" PRIMARY KEY (id));

-- changeset aleksandr_pekarskiy:1.2.1.2
ALTER TABLE booking ADD CONSTRAINT UC_BOOKINGBOOKING_NUMBER_COL UNIQUE (booking_number);

-- changeset aleksandr_pekarskiy:1.2.1.3
ALTER TABLE booking ADD CONSTRAINT "FK6diagj5g1i57rnn5o2sgmcy9x" FOREIGN KEY (passenger_id) REFERENCES passengers (id);

-- changeset aleksandr_pekarskiy:1.2.1.4
ALTER TABLE booking ADD CONSTRAINT "FKbswcjfeskx6oiyq64d7fm7a6g" FOREIGN KEY (flight_seat_id) REFERENCES flight_seats (id);

-- changeset aleksandr_pekarskiy:1.2.1.5
ALTER TABLE booking ADD CONSTRAINT "FKdgruopua1epsrvwevfucgfno6" FOREIGN KEY (flight_id) REFERENCES flights (id);