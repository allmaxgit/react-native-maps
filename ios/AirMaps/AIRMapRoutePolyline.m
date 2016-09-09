//
//  AIRMapRoutePolyline.m
//  AirMaps
//
//  Created by Taras Parkhomenko on 09/09/2016.
//  Copyright © 2016 Christopher. All rights reserved.
//

#import "AIRMapRoutePolyline.h"

@implementation AIRMapRoutePolyline

-(void) setRoute: (NSArray<AIRMapCoordinate *> *)route {
    
    AIRMapCoordinate* start = [route objectAtIndex:0];
    AIRMapCoordinate* finish = [route objectAtIndex:1];
    
    MKDirectionsRequest* request = [[MKDirectionsRequest alloc] init];
    
    MKMapItem* source = [[MKMapItem alloc] initWithPlacemark:[[MKPlacemark alloc] initWithCoordinate:[start coordinate] addressDictionary:nil]];
    MKMapItem* destination = [[MKMapItem alloc] initWithPlacemark:[[MKPlacemark alloc] initWithCoordinate:[finish coordinate] addressDictionary:nil]];
    
    [request setSource: source];
    [request setDestination: destination];
    [request setTransportType: MKDirectionsTransportTypeAny];
    
    MKDirections* directions = [[MKDirections alloc] initWithRequest:request];
    [directions calculateDirectionsWithCompletionHandler:^(MKDirectionsResponse * _Nullable response, NSError * _Nullable error) {
        if (response) {
            
            MKPolyline* direction = [[[response routes] objectAtIndex:0] polyline];
            
            if (direction)
                [self setPolylineAndUpdate:direction];
        } else {
            RCTLogWarn([error description]);
        }
    }];
}

@end
