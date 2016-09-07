//
//  AIRGoogleMapMarker.m
//  AirMaps
//
//  Created by Gil Birman on 9/2/16.
//

#import "AIRGoogleMapMarker.h"
#import <GoogleMaps/GoogleMaps.h>
#import "AIRGMSMarker.h"
#import "AIRGoogleMapCallout.h"
#import "RCTImageLoader.h"
#import "RCTUtils.h"

@implementation AIRGoogleMapMarker {
  RCTImageLoaderCancellationBlock _reloadImageCancellationBlock;
}

- (instancetype)init
{
  if ((self = [super init])) {
    _realMarker = [[AIRGMSMarker alloc] init];
    _realMarker.title = @"AAAAA";
    _realMarker.snippet = @"BBBBBB";
  }
  return self;
}

- (void)insertReactSubview:(id<RCTComponent>)subview atIndex:(NSInteger)atIndex {
  if ([subview isKindOfClass:[AIRGoogleMapCallout class]]) {
//    self.calloutView = (AIRMapCallout *)subview;
    printf("TODO: Callout\n");
  } else {
    // Custom UIView Marker
    // NOTE: Originally I tried creating a new UIView here to encapsulate subview,
    //       but it would not sizeToFit properly. Not sure why.
    UIView *v = (UIView*)subview;
    [super insertReactSubview:subview atIndex:atIndex];
    [self sizeToFit];

    // TODO: how to handle this circular reference properly?
    _realMarker.iconView = self;
  }
}

- (void)layoutSubviews {
  printf("layout subviews\n");
}

- (void)setCoordinate:(CLLocationCoordinate2D)coordinate {
  _realMarker.position = coordinate;
}

- (CLLocationCoordinate2D)coordinate {
  return _realMarker.position;
}

- (void)setIdentifier:(NSString *)identifier {
  _realMarker.identifier = identifier;
}

- (NSString *)identifier {
  return _realMarker.identifier;
}

- (void)setOnPress:(RCTBubblingEventBlock)onPress {
  _realMarker.onPress = onPress;
}

- (RCTBubblingEventBlock)onPress {
  return _realMarker.onPress;
}

- (void)setImageSrc:(NSString *)imageSrc
{
  _imageSrc = imageSrc;

  if (_reloadImageCancellationBlock) {
    _reloadImageCancellationBlock();
    _reloadImageCancellationBlock = nil;
  }
  _reloadImageCancellationBlock = [_bridge.imageLoader loadImageWithURLRequest:[RCTConvert NSURLRequest:_imageSrc]
                                                                          size:self.bounds.size
                                                                         scale:RCTScreenScale()
                                                                       clipped:YES
                                                                    resizeMode:UIViewContentModeCenter
                                                                 progressBlock:nil
                                                               completionBlock:^(NSError *error, UIImage *image) {
                                                                 if (error) {
                                                                   // TODO(lmr): do something with the error?
                                                                   NSLog(@"%@", error);
                                                                 }
                                                                 dispatch_async(dispatch_get_main_queue(), ^{
                                                                   UIImageView *imageView = [[UIImageView alloc] initWithImage:image];

                                                                   // TODO: w,h or pixel density could be a prop.
                                                                   float density = 2;
                                                                   float w = image.size.width/density;
                                                                   float h = image.size.height/density;
                                                                   [imageView setFrame:CGRectMake(0, 0, w, h)];

//                                                                   UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 200, 200)];
//                                                                   v.backgroundColor = [UIColor redColor];
//                                                                   _realMarker.iconView = v;
                                                                   _realMarker.iconView = imageView;
//                                                                   _realMarker.iconView.bounds = CGRectMake(0, 0, image.size.width/2, image.size.height/2);

                                                                   // TODO: This could be a prop
                                                                   //_realMarker.groundAnchor = CGPointMake(0.75, 1);
                                                                 });
                                                               }];
}

@end
