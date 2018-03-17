function init() {
	var api = "http://localhost:9000/api/";

	Highcharts.setOptions({
		global: {
			useUTC: false
		}
	});
	
	var chart = Highcharts.chart('chart', {
		chart: {
			type: 'spline',
			animation: Highcharts.svg, // don't animate in old IE
			marginRight: 10,
			events: { load: registerListeners }
		},
		title: {
			text: 'Java Processes CPU Load'
		},
		xAxis: {
			type: 'datetime',
			tickPixelInterval: 150
		},
		yAxis: {
			title: {
				text: 'CPU Load'
			},
			plotLines: [{
				value: 0,
				width: 1,
				color: '#808080'
			}]
		},
		tooltip: {
			formatter: function () {
				return '<b>' + this.series.name + '</b><br/>' +
					Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
					Highcharts.numberFormat(this.y, 2);
			}
		},
		legend: {
			layout: 'vertical',
			align: 'right',
			verticalAlign: 'middle'
		},
		exporting: {
			enabled: false
		},
		series: []
	});

	function registerListeners() {
		if (!!window.EventSource) {
			var eventSource = new EventSource(api + "cpu");

			eventSource.onmessage = function(e) {
				var datapoint = JSON.parse(e.data)
				console.log(datapoint);

				var index = chart.series.findIndex(serie => serie.name == datapoint.pid);
				if(index > 0) {
					var serie = chart.series[index];
					var shift = serie.data.length > 20;
					serie.addPoint([datapoint.instant, datapoint.load], true, shift);
				}

				// request new processes info if processes have changed
				if (datapoint.changed) {
					updateProcesses();
				}
			};

			eventSource.addEventListener('open', function(e) {
				console.log('Opened: ' +e);
				}, false);

			$('#content').bind('unload', function() {
				eventSource.close();
			});

			updateProcesses();
		}
	}

	function updateProcesses() {
		$.getJSON(api + "processes")
			.done(function(data) {
				var items = [];
				var series = [];
				data.forEach( info => {
					items.push("<li><span class='pid'>PID: " + info.pid
						+ "</span> - <span class='command'>" + info.command + "</span></li>");
					var existing = chart.series.find( serie => serie.name === info.pid);
					console.log(existing);
					var existingData = [];
					if(existing) {
					    existingData = existing.data.map( point => ({x: point.x, y: point.y}));
					}
//					console.log(existingData);
                    series.push( { name: info.pid, data: existingData } );
			    });
			    chart.update({series: series}, true, true);
				$("#processes").html(items.join(""));
		});
	}
}