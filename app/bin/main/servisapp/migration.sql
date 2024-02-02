CREATE TABLE IF NOT EXISTS Plata (
    id INT AUTO_INCREMENT PRIMARY KEY,
    month VARCHAR(255),
    plata INT
);

CREATE TABLE IF NOT EXISTS Materijal (
    id INT AUTO_INCREMENT PRIMARY KEY,
    imeDela VARCHAR(255),
    kataloskiBroj VARCHAR(255),
    kolicina INT,
    cenaDela INT,
    report_id INT,
    FOREIGN KEY (report_id) REFERENCES Report(id)
);

CREATE TABLE IF NOT EXISTS Stavka (
    id INT AUTO_INCREMENT PRIMARY KEY,
    imeStavke VARCHAR(255),
    brojSati INT
    report_id INT,
    FOREIGN KEY (report_id) REFERENCES Report(id)
);

CREATE TABLE IF NOT EXISTS Report (
    id INT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(255),
    registarskiBroj VARCHAR(255),
    telefon VARCHAR(255),
    brojSasije VARCHAR(255),
    brojMotora VARCHAR(255),
    boja VARCHAR(255),
    godiste VARCHAR(255),
    serviser VARCHAR(255),
    datum DATE,
    ruke INT,
    cenaDelova INT,
    PRIMARY KEY (brojSasije, brojMotora, registarskiBroj),
);