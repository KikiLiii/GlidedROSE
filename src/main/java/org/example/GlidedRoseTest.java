package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlidedRoseTest {

    private Item[] items;
    private GlidedRose gildedRose;

    @BeforeEach
    void setUp() {
        items = new Item[]{
                new Item("+5 Dexterity Vest", 10, 20),
                new Item("Aged Brie", 2, 0),
                new Item("Elixir of the Mongoose", 5, 7),
                new Item("Sulfuras, Hand of Ragnaros", 0, 80),
                new Item("Sulfuras, Hand of Ragnaros", -1, 80),
                new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
                new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
                new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
                new Item("Conjured Mana Cake", 3, 6)
        };
        gildedRose = new GlidedRose(items);
    }

    @Test
    void testQualityDecreasesNormalItem() {
        gildedRose.updateQuality();
        assertEquals(19, items[0].quality);
    }

    @Test
    void testQualityIncreasesAgedBrie() {
        gildedRose.updateQuality();
        assertEquals(1, items[1].quality);
    }

    @Test
    void testQualityRemainsSulfuras() {
        gildedRose.updateQuality();
        assertEquals(80, items[3].quality);
    }

    @Test
    void testQualityIncreasesBackstagePasses11DaysLeft() {
        gildedRose.updateQuality();
        assertEquals(21, items[5].quality);
    }

    @Test
    void testQualityIncreasesBackstagePasses10DaysLeft() {
        items[5].sellIn = 10; // Setting sellIn for this test
        gildedRose.updateQuality();
        assertEquals(22, items[5].quality);
    }

    @Test
    void testQualityIncreasesBackstagePasses6DaysLeft() {
        items[5].sellIn = 6; // Setting sellIn for this test
        gildedRose.updateQuality();
        assertEquals(23, items[5].quality);
    }

    @Test
    void testQualityIncreasesBackstagePasses5DaysLeft() {
        items[5].sellIn = 5; // Setting sellIn for this test
        gildedRose.updateQuality();
        assertEquals(24, items[5].quality);
    }

    @Test
    void testQualityDecreasesConjuredItems() {
        gildedRose.updateQuality();
        assertEquals(5, items[8].quality); // Conjured item should degrade twice as fast
    }

    @Test
    void testQualityNeverNegative() {
        items[0].quality = 0; // Set quality to 0
        gildedRose.updateQuality();
        assertEquals(0, items[0].quality); // Quality should not go below 0
    }

    @Test
    void testQualityNeverExceeds50AgedBrie() {
        items[1].quality = 50; // Set quality to 50
        gildedRose.updateQuality();
        assertEquals(50, items[1].quality); // Quality should not exceed 50
    }

    @Test
    void testQualityBackstagePasses0DaysLeft() {
        items[5].sellIn = 0; // Expired
        gildedRose.updateQuality();
        assertEquals(0, items[5].quality); // Quality should drop to 0
    }

    @Test
    void testQualityDecreaseSulfurasNeverChanges() {
        items[4].sellIn = -1; // Should not affect quality
        gildedRose.updateQuality();
        assertEquals(80, items[4].quality); // Quality should remain 80
    }

    @Test
    void testQualityDecreasesAfterOneDay() {
        gildedRose.updateQuality(); // Day 1
        gildedRose.updateQuality(); // Day 2
        assertEquals(18, items[0].quality); // Check quality after 2 days
    }

    @Test
    void testMultipleDaysQuality() {
        for (int i = 0; i < 5; i++) {
            gildedRose.updateQuality(); // Update for 5 days
        }
        assertEquals(15, items[0].quality); // Expected quality after 5 days
    }

    @Test
    void testQualityOfExpiredItems() {
        items[0].sellIn = -1; // Expired item
        gildedRose.updateQuality();
        assertEquals(19, items[0].quality); // Quality should decrease by 2 instead of 1
    }

    @Test
    void testQualityStaysSameForNegativeSellInSulfuras() {
        gildedRose.updateQuality();
        assertEquals(80, items[3].quality); // Check that it stays the same
    }

    @Test
    void testSellInDecreases() {
        gildedRose.updateQuality();
        assertEquals(9, items[0].sellIn); // SellIn should decrease by 1
    }
}
