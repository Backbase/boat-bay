import { Component, Input, OnInit } from '@angular/core';
import { BoatStatistics } from 'app/models/dashboard/boat-dashboard';

@Component({
  selector: 'bb-statistics-current',
  templateUrl: './bb-statistics-current.component.html',
  styleUrls: ['./bb-statistics-current.component.scss'],
})
export class BbStatisticsCurrentComponent implements OnInit {
  @Input()
  statistics!: BoatStatistics;

  constructor() {}

  ngOnInit(): void {}
}
