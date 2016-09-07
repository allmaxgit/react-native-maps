//
//  AIRGoogleMapMarker.h
//  AirMaps
//
//  Created by Gil Birman on 9/2/16.
//

#import <GoogleMaps/GoogleMaps.h>
#import "AIRGMSMarker.h"
#import "RCTBridge.h"
#import "AIRGoogleMap.h"

@interface AIRGoogleMapMarker : UIView

@property (nonatomic, weak) RCTBridge *bridge;
//@property (nonatomic, weak) AIRGoogleMap *map;
@property (nonatomic, strong) NSString *identifier;
@property (nonatomic, assign) CLLocationCoordinate2D coordinate;
@property (nonatomic, strong) AIRGMSMarker* realMarker;
@property (nonatomic, copy) RCTBubblingEventBlock onPress;
@property (nonatomic, copy) NSString *imageSrc;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *subtitle;

- (void)showCalloutView;
- (void)hideCalloutView;

@end
