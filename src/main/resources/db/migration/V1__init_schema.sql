-- ZenDrive Initial Schema

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    role VARCHAR(20) NOT NULL DEFAULT 'CLIENT',
    enabled BOOLEAN NOT NULL DEFAULT true,
    verification_token VARCHAR(255),
    reset_token VARCHAR(255),
    reset_token_expiry TIMESTAMP,
    profile_image VARCHAR(500),
    preferred_language VARCHAR(10) DEFAULT 'fr',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE vehicles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    image_url VARCHAR(500),
    passenger_capacity INT NOT NULL,
    baggage_capacity INT NOT NULL,
    comfort_level VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    price_per_km DECIMAL(10,2) NOT NULL,
    price_per_hour DECIMAL(10,2),
    base_fare DECIMAL(10,2),
    license_plate VARCHAR(50),
    description TEXT,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE excursions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image_url VARCHAR(500),
    type VARCHAR(20) NOT NULL,
    base_price DECIMAL(10,2) NOT NULL,
    estimated_duration_hours DECIMAL(10,2),
    zone VARCHAR(255),
    active BOOLEAN DEFAULT true,
    min_passengers INT DEFAULT 1,
    max_passengers INT DEFAULT 50,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE excursion_steps (
    id BIGSERIAL PRIMARY KEY,
    excursion_id BIGINT NOT NULL REFERENCES excursions(id) ON DELETE CASCADE,
    step_order INT NOT NULL,
    location_name VARCHAR(255) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    description TEXT,
    duration_minutes DECIMAL(10,2),
    image_url VARCHAR(500)
);

CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    booking_reference VARCHAR(50) NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    client_id BIGINT NOT NULL REFERENCES users(id),
    vehicle_id BIGINT REFERENCES vehicles(id),
    excursion_id BIGINT REFERENCES excursions(id),
    pickup_location VARCHAR(500),
    pickup_latitude DOUBLE PRECISION,
    pickup_longitude DOUBLE PRECISION,
    dropoff_location VARCHAR(500),
    dropoff_latitude DOUBLE PRECISION,
    dropoff_longitude DOUBLE PRECISION,
    transfer_type VARCHAR(20),
    pickup_date DATE NOT NULL,
    pickup_time TIME NOT NULL,
    return_date DATE,
    return_time TIME,
    passenger_count INT DEFAULT 1,
    estimated_distance DECIMAL(10,2),
    estimated_duration DECIMAL(10,2),
    total_price DECIMAL(10,2) NOT NULL,
    discount_amount DECIMAL(10,2),
    flight_number VARCHAR(50),
    airline_name VARCHAR(255),
    guest_email VARCHAR(255),
    guest_phone VARCHAR(50),
    guest_name VARCHAR(255),
    special_requests TEXT,
    admin_notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    cancellation_reason TEXT
);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    reservation_id BIGINT NOT NULL REFERENCES reservations(id),
    amount DECIMAL(10,2) NOT NULL,
    refunded_amount DECIMAL(10,2),
    payment_method VARCHAR(20) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    transaction_id VARCHAR(255),
    stripe_payment_intent_id VARCHAR(255),
    paypal_order_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    paid_at TIMESTAMP,
    refunded_at TIMESTAMP
);

CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL REFERENCES users(id),
    reservation_id BIGINT NOT NULL REFERENCES reservations(id),
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    admin_response TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    moderated_at TIMESTAMP
);

CREATE TABLE loyalty_accounts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id),
    total_points INT NOT NULL DEFAULT 0,
    available_points INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE loyalty_transactions (
    id BIGSERIAL PRIMARY KEY,
    loyalty_account_id BIGINT NOT NULL REFERENCES loyalty_accounts(id),
    points INT NOT NULL,
    description VARCHAR(500) NOT NULL,
    reference_type VARCHAR(50),
    reference_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE coupons (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    discount_percentage DECIMAL(5,2) NOT NULL,
    max_discount_amount DECIMAL(10,2),
    min_order_amount DECIMAL(10,2),
    max_usage_count INT NOT NULL DEFAULT 100,
    current_usage_count INT NOT NULL DEFAULT 0,
    single_use_per_client BOOLEAN DEFAULT false,
    active BOOLEAN NOT NULL DEFAULT true,
    valid_from TIMESTAMP NOT NULL,
    valid_until TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    type VARCHAR(50) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    subject VARCHAR(500) NOT NULL,
    content TEXT NOT NULL,
    recipient VARCHAR(255),
    sent BOOLEAN DEFAULT false,
    read BOOLEAN DEFAULT false,
    sent_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE routes (
    id BIGSERIAL PRIMARY KEY,
    origin_name VARCHAR(255) NOT NULL,
    origin_latitude DOUBLE PRECISION,
    origin_longitude DOUBLE PRECISION,
    destination_name VARCHAR(255) NOT NULL,
    destination_latitude DOUBLE PRECISION,
    destination_longitude DOUBLE PRECISION,
    distance_km DECIMAL(10,2),
    estimated_duration_minutes DECIMAL(10,2),
    base_price DECIMAL(10,2),
    popular BOOLEAN DEFAULT false,
    active BOOLEAN DEFAULT true,
    image_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id BIGINT,
    performed_by BIGINT,
    performed_by_email VARCHAR(255),
    details TEXT,
    ip_address VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_reservations_client_id ON reservations(client_id);
CREATE INDEX idx_reservations_status ON reservations(status);
CREATE INDEX idx_reservations_type ON reservations(type);
CREATE INDEX idx_reservations_pickup_date ON reservations(pickup_date);
CREATE INDEX idx_reservations_booking_ref ON reservations(booking_reference);
CREATE INDEX idx_payments_reservation_id ON payments(reservation_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_reviews_client_id ON reviews(client_id);
CREATE INDEX idx_reviews_status ON reviews(status);
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_audit_logs_entity ON audit_logs(entity_type, entity_id);

-- Insert default admin user (password: admin123)
INSERT INTO users (first_name, last_name, email, password, role, enabled, created_at)
VALUES ('Admin', 'ZenDrive', 'admin@zendrive.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'ADMIN', true, NOW());

-- Insert sample vehicles
INSERT INTO vehicles (name, brand, model, passenger_capacity, baggage_capacity, comfort_level, status, price_per_km, price_per_hour, base_fare, active, created_at)
VALUES
    ('Berline Standard', 'Mercedes', 'Classe E', 3, 3, 'STANDARD', 'AVAILABLE', 1.50, 30.00, 15.00, true, NOW()),
    ('Van Confort', 'Mercedes', 'Vito', 7, 7, 'PREMIUM', 'AVAILABLE', 2.00, 45.00, 20.00, true, NOW()),
    ('Minibus VIP', 'Mercedes', 'Sprinter', 16, 16, 'VIP', 'AVAILABLE', 2.50, 60.00, 30.00, true, NOW()),
    ('SUV Luxe', 'BMW', 'X5', 4, 4, 'LUXURY', 'AVAILABLE', 3.00, 55.00, 25.00, true, NOW()),
    ('Berline Premium', 'Audi', 'A6', 3, 3, 'PREMIUM', 'AVAILABLE', 2.00, 40.00, 18.00, true, NOW());

-- Insert sample routes
INSERT INTO routes (origin_name, destination_name, distance_km, estimated_duration_minutes, base_price, popular, active, created_at)
VALUES
    ('Aeroport Tunis-Carthage', 'Centre-ville Tunis', 10.00, 20.00, 25.00, true, true, NOW()),
    ('Aeroport Tunis-Carthage', 'La Marsa', 18.00, 30.00, 35.00, true, true, NOW()),
    ('Aeroport Tunis-Carthage', 'Hammamet', 70.00, 60.00, 80.00, true, true, NOW()),
    ('Aeroport Tunis-Carthage', 'Sousse', 140.00, 120.00, 130.00, true, true, NOW()),
    ('Aeroport Tunis-Carthage', 'Sidi Bou Said', 15.00, 25.00, 30.00, true, true, NOW()),
    ('Aeroport Tunis-Carthage', 'Monastir', 160.00, 140.00, 150.00, false, true, NOW());

-- Insert sample excursions
INSERT INTO excursions (name, description, type, base_price, estimated_duration_hours, zone, active, min_passengers, max_passengers, created_at)
VALUES
    ('Carthage & Sidi Bou Said', 'Decouvrez les ruines de Carthage et le charmant village de Sidi Bou Said', 'PACKAGED', 75.00, 4.00, 'Tunis Nord', true, 1, 8, NOW()),
    ('Sahara Adventure', 'Une journee complete dans le desert du Sahara avec balade en chameau', 'PACKAGED', 180.00, 12.00, 'Sud Tunisie', true, 2, 16, NOW()),
    ('Tour Medina Tunis', 'Explorez la medina historique de Tunis, classee patrimoine mondial UNESCO', 'PACKAGED', 45.00, 3.00, 'Tunis Centre', true, 1, 10, NOW()),
    ('Excursion Sur Mesure', 'Creez votre propre itineraire personnalise', 'CUSTOM', 100.00, 8.00, 'Toute la Tunisie', true, 1, 50, NOW());
