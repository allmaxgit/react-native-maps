//
//  AIRMapRoutePolylineManager.m
//  AirMaps
//
//  Created by Taras Parkhomenko on 09/09/2016.
//  Copyright © 2016 Christopher. All rights reserved.
//

#import "AIRMapRoutePolylineManager.h"

#import "RCTBridge.h"
#import "RCTConvert.h"
#import "RCTConvert+CoreLocation.h"
#import "RCTConvert+MoreMapKit.h"
#import "RCTEventDispatcher.h"
#import "UIView+React.h"
#import "AIRMapMarker.h"
#import "RCTViewManager.h"
#import "AIRMapRoutePolyline.h"

@interface AIRMapRoutePolylineManager()

@end

@implementation AIRMapRoutePolylineManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
    AIRMapRoutePolyline *polyline = [AIRMapRoutePolyline new];
    return polyline;
}

RCT_EXPORT_VIEW_PROPERTY(route, AIRMapCoordinateArray)
RCT_EXPORT_VIEW_PROPERTY(strokeColor, UIColor)
RCT_EXPORT_VIEW_PROPERTY(strokeWidth, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(lineCap, CGLineCap)
RCT_EXPORT_VIEW_PROPERTY(lineJoin, CGLineJoin)
RCT_EXPORT_VIEW_PROPERTY(miterLimit, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(lineDashPhase, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(lineDashPattern, NSArray)

// NOTE(lmr):
// for now, onPress events for overlays will be left unimplemented. Seems it is possible with some work, but
// it is difficult to achieve in both ios and android so I decided to leave it out.
//RCT_EXPORT_VIEW_PROPERTY(onPress, RCTBubblingEventBlock)

@end
