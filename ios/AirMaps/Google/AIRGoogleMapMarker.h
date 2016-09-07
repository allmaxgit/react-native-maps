//
//  AIRGoogleMapMarker.h
//  AirMaps
//
//  Created by Gil Birman on 9/2/16.
//

#import <GoogleMaps/GoogleMaps.h>
#import "AIRGMSMarker.h"
#import "RCTBridge.h"

@interface AIRGoogleMapMarker : UIView

@property (nonatomic, weak) RCTBridge *bridge;
@property (nonatomic, strong) NSString *identifier;
@property (nonatomic, assign) CLLocationCoordinate2D coordinate;
@property (nonatomic, strong) AIRGMSMarker* realMarker;
@property (nonatomic, copy) RCTBubblingEventBlock onPress;
@property (nonatomic, copy) NSString *imageSrc;

@end
