//
//  ViewController.swift
//  Onnikka
//
//  Created by Ville Marttila on 3.2.2017.
//  Copyright © 2017 Foxware. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    // MARK: Properties
    @IBOutlet weak var temperatureLabel: UILabel!
    @IBOutlet weak var humidityLabel: UILabel!
    @IBOutlet weak var airQualityLabel: UILabel!
    
    let urlString = "https://api.thingspeak.com/channels/180604/feeds.json?results=1"
    
    override func viewDidLoad() {
        super.viewDidLoad()
    
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    func loadFromJSON() -> [String] {
        
        print("Loading from JSON...")
        
        var temperatures = [String]()
        var humidities = [String]()
        var airQualities = [String]()
        
        let url = URL(string: urlString)
        URLSession.shared.dataTask(with:url!) { (data, response, error) in
            if error != nil {
                print(error!)
            } else {
                
                do {
                    if let data = data,
                        let json = try JSONSerialization.jsonObject(with: data) as? [String: Any],
                        let feeds = json["feeds"] as? [[String: Any]] {
                        for feed in feeds {
                            if let temperature = feed["field1"] as? String {
                                temperatures.append(temperature)
                            }
                            if let humidity = feed["field2"] as? String {
                                humidities.append(humidity)
                            }
                            if let airQuality = feed["field3"] as? String {
                                airQualities.append(airQuality)
                            }
                            
                        }
                    }
                } catch {
                    print("Error deserializing JSON: \(error)")
                }
                
                print("Temperatures: \(temperatures) \nHumidites: \(humidities)\nAir quality: \(airQualities)")
                self.temperatureLabel.text = "Lämpötila on \(temperatures[0])°C"
                self.humidityLabel.text = "Ilmankosteus on \(humidities[0])%"
                self.airQualityLabel.text = "Ilmanlaatu on \(airQualities[0])"
                
            }
            
            }.resume()
        return airQualities
    }
    
    
    func setAirQuality(airQuality: String) {
        // TODO - Create AirQuality filter here.
    }
    
    
}

