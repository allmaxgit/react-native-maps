//
//  AIRMapRoutePolyline.h
//  AirMaps
//
//  Created by Taras Parkhomenko on 09/09/2016.
//  Copyright © 2016 Christopher. All rights reserved.
//

#import "AIRMapPolyline.h"

@interface AIRMapRoutePolyline : AIRMapPolyline

@property (nonatomic, strong) NSArray<AIRMapCoordinate *> *route;

@end
