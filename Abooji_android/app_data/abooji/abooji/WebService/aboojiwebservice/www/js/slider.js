$(function(){
	//Featured Slide
	var featuredSwiper = $('.featured').swiper({
		slidesPerView:'auto',
		centeredSlides: true,
		initialSlide:5,
		tdFlow: {
			rotate : 30,
			stretch :3,
			depth: 150
		}
	})
	
	//Thumbs
	$('.thumbs-cotnainer').each(function(){
		$(this).swiper({
			slidesPerView:'auto',
			offsetPxBefore:25,
			offsetPxAfter:10,
			calculateHeight: true
		})
	})

	//Banners
	$('.banners-container').each(function(){
		$(this).swiper({
			slidesPerView:'auto',
			offsetPxBefore:25,
			offsetPxAfter:10
		})	
	})
})
